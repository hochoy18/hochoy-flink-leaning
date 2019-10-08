package com.hochoy.flink.socket;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class WindowWordCount {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        DataStream<Tuple2<String, Long>> sum = env
                .socketTextStream("localhost", 9999)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                        Arrays.asList(value.split(" ")).stream().forEach(e -> out.collect(new Tuple2<>(e, 1L)));
                    }
                })
                .keyBy(0)
                .timeWindow(Time.milliseconds(1000 * 3))
                .sum(1);

        sum.print();

        try {
            env.execute(WindowWordCount.class.getName());
        }catch (Exception e ){
            e.printStackTrace();
        }


    }

}
