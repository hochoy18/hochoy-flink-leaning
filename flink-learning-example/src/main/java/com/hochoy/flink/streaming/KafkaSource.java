package com.hochoy.flink.streaming;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;


public class KafkaSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String topic = "hochoy_flink";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "flink-kafka-demo");

        DataStreamSource<String> source = env.addSource(new FlinkKafkaConsumer011<>(topic, new SimpleStringSchema(), props));
        SingleOutputStreamOperator<JSONObject> map = source
                .filter((record) -> null != record && !"".equals(record))
                .map(JSON::parseObject);
        SingleOutputStreamOperator<Tuple2<String, Double>> map1 = map.map(record -> {
            String type = record.getString("type");
            JSONObject data = record.getJSONObject("data");
            System.out.println("data>>>>>>>>>>>>>>>  " + data.toJSONString());

            String name = data.getString("name");
            JSONObject fields = data.getJSONObject("fields");
            Double max = fields.getDouble("max");

            return Tuple2.of(name,max);
        }).returns(Types.TUPLE(Types.STRING, Types.DOUBLE));

        map1.print();

        SingleOutputStreamOperator<Tuple2<String, Double>> max = map1.keyBy(0).max(1);
        max.print("max >>>>>>   ");

        env.execute("flink learning connectors kafka");
    }

}
