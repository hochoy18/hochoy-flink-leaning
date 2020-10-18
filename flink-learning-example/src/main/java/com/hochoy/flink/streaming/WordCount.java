package com.hochoy.flink.streaming;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.RichFlatMapFunction;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.Properties;

public class WordCount {

    private static StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args) throws Exception {
        ParameterTool tool = ParameterTool.fromArgs(args);
        String type = tool.get("type");

        switch (type){

            case "socket":
                /**
                 *   --type socket  --host localhost  --port 9999
                 *   nc -l -k 9999
                 */
                String host = tool.get("host");
                int port = tool.getInt("port");
                socket(host,port);
                break;
            case "kafka": {
                /**
                 * --type kafka --bootstrap.server localhost:9092  --topic part3-topic
                 */
                String bootstrapServer = tool.get("bootstrap.server");
                String topic = tool.get("topic");
                Properties props = new Properties();
                props.setProperty("bootstrap.servers", bootstrapServer);
                props.setProperty("group.id", "kafka-demo");
                kafka(topic, props);
                break;
            }

            case "sinkKafka": {
                /**
                 *  // flink-sink-topic
                 *  --type sinkKafka --bootstrap.server localhost:9092  --sourceTopic part3-topic --sinkTopic flink-sink-topic
                 */
                String sourceTopic = tool.get("sourceTopic");
                String bootstrapServer = tool.get("bootstrap.server");
                String sinkTopic = tool.get("sinkTopic");

                Properties props = new Properties();
                props.setProperty("bootstrap.servers", bootstrapServer);
                props.setProperty("group.id", "kafka-demo");
                kafkaSink(props, sinkTopic, sourceTopic);
                break;
            }

        }
        streamEnv.execute(WordCount.class.getName() + "-" + type);



    }


    private static void kafkaSink(Properties props,String sinkTopic,String sourceTopic){
        DataStreamSource<String> source = streamEnv.addSource(new FlinkKafkaConsumer011<>(
                sourceTopic, new SimpleStringSchema(),
                props
        ));
//        source.print();

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> map = source.filter(e -> e != null && !e.trim().equals(""))
                .map(JSON::parseObject)
                .map(e -> Tuple3.of(e.getString("subject"), e.getString("name"), e.getIntValue("score")))
                .returns(Types.TUPLE(Types.STRING,Types.STRING,Types.INT));
//        map.print();
        SingleOutputStreamOperator<Tuple2<String, Integer>> subjectSum =
                map.map(e -> Tuple2.of(e.f0, e.f2)).returns(Types.TUPLE(Types.STRING,Types.INT))
                        .keyBy(0)
                        .sum(1)
                        .returns(Types.TUPLE(Types.STRING,Types.INT));

        SingleOutputStreamOperator<String> res1 = subjectSum.map(e -> {
            JSONObject jo = new JSONObject();
            jo.put("subject", e.f0);
            jo.put("score", e.f1);
            return jo;
        }).map(JSONAware::toJSONString).returns(Types.STRING);

        res1.print();
        res1.addSink(new FlinkKafkaProducer011<>(
                props.getProperty("bootstrap.servers"),
                sinkTopic,
                new SimpleStringSchema()));
    }

    private static void kafka(String topic, Properties properties){
        streamEnv.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);

        DataStreamSource<String> source = streamEnv.addSource(new FlinkKafkaConsumer011<>(
                topic, new SimpleStringSchema(),
                properties
        ));
        SingleOutputStreamOperator<JSONObject> map = source.map(new RichMapFunction<String, JSONObject>() {
            @Override
            public JSONObject map(String value) {
                JSONObject res;
                try {
                    res = JSONObject.parseObject(value.trim());
                } catch (JSONException e) {
                    return null;
                }
                return res;
            }
        });
        SingleOutputStreamOperator<Tuple3<String, String, Double>> scores = map.map(e -> {
            String name = e.getString("name");
            Double score = e.getDouble("score");
            String subject = e.getString("subject");
            return Tuple3.of(subject, name, score);
        }).returns(Types.TUPLE(Types.STRING, Types.STRING, Types.DOUBLE));

        // 某一个科目 所有学生的成绩
        SingleOutputStreamOperator<Tuple3<String, String, Double>> sum = scores.keyBy(0).sum(2);
        sum.print();

        // 学生的总成绩
        SingleOutputStreamOperator<Tuple3<String, String, Double>> sumIndividual = scores.keyBy(1).sum(2);
        sumIndividual.print();
    }


    private static void  socket(String host,int port){

        DataStreamSource<String> source = streamEnv.socketTextStream(host, port, "\n");

        source.print();
        SingleOutputStreamOperator<String> words = source.flatMap(new RichFlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                Arrays.stream(value.trim().split("\\s+"))
                        .forEach(out::collect);
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne =
                words.map(word -> Tuple2.of(word,1))
                .returns(Types.TUPLE(Types.STRING,Types.INT));
        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = wordAndOne.keyBy(0);

//        keyedStream.print();

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);
        sum.print();

    }
}
