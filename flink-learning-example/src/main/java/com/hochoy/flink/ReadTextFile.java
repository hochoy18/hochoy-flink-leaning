package com.hochoy.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class ReadTextFile {
//    public ReadTextFile() {
//    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> dataSource = env.readTextFile(System.getProperty("user.dir") + "\\test_data\\test1\\WordCount.txt");

        FlatMapOperator<String, String> flatMap = dataSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                Arrays.stream(value.split(" ")).forEach(out::collect);
            }
        });

        flatMap.print();
        System.out.println("------------------------------");

        MapOperator<String, Tuple2<String, Integer>> map = flatMap.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String value) throws Exception {
                return   Tuple2.of(value, 1);
            }
        });

        map.print();
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        ReduceOperator<Tuple2<String, Integer>> reduce = map.groupBy(1).reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
            }
        });
        System.out.println("reduce========================");
        reduce.print();




        dataSource
                .flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                        Arrays.asList(value.split(" ")).stream().forEach(e -> out.collect(new Tuple2<>(e, 1l)));
                    }
                });
        try {
            dataSource.print();
//            env.execute("read text file batch ");
        } catch (Exception e) {
            e.printStackTrace();
        }


//        dataSource
//                .groupBy(0)
//                .combineGroup(new GroupCombineFunction<String, Tuple2<String, Long>>() {
//                    @Override
//                    public void combine(Iterable<String> values, Collector<Tuple2<String, Long>> out) throws Exception {
//                        String key = null;
//                        int count = 0;
//
//                        for (String word : values) {
//                            key = word;
//                            count++;
//                        }
//                        // emit tuple with word and count
//                        out.collect(new Tuple2(key, count));
//                    }
//                });
    }
}
