package com.hochoy.flink.streaming;

import org.apache.flink.api.common.functions.RichFlatMapFunction;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

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
        }
        streamEnv.execute(WordCount.class.getName() + "-" + type);



    }

    private static void  socket(String host,int port){

        DataStreamSource<String> source = streamEnv.socketTextStream(host, port, "\n");

        System.out.println("==================> source");
        source.print();
        System.out.println("==================> source");
        SingleOutputStreamOperator<String> words = source.flatMap(new RichFlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                Arrays.stream(value.split(" "))
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
