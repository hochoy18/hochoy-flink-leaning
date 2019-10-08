package com.hochoy.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.GroupCombineFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.translation.CombineToGroupCombineWrapper;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class ReadTextFile {
    public static void main(String[] args) {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> dataSource = env.readTextFile(System.getProperty("user.dir") + "\\test_data\\test1\\WordCount.txt");

        dataSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                Arrays.asList(value.split(" ")).stream().forEach(e -> out.collect(e));
            }
        }).map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                return new Tuple2<String, Long>(value, 1L);
            }
        }).groupBy(1);

        dataSource
                .flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                        Arrays.asList(value.split(" ")).stream().forEach(e -> out.collect(new Tuple2<>(e, 1l)));
                    }
                });


        dataSource
                .groupBy(0)
                .combineGroup(new GroupCombineFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void combine(Iterable<String> values, Collector<Tuple2<String, Long>> out) throws Exception {
                        String key = null;
                        int count = 0;

                        for (String word : values) {
                            key = word;
                            count++;
                        }
                        // emit tuple with word and count
                        out.collect(new Tuple2(key, count));
                    }
                });
    }
}
