package com.hochoy.flink.join;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class WindowJoin {

    public static void main(String[] args) {
        ParameterTool params = ParameterTool.fromArgs(args);

        long windowSize = params.getLong("windowSize", 2000);

        long rate = params.getLong("rate", 3L);

        System.out.println("Using windowSize = " + windowSize + " , data rate = " + rate);
        System.out.println("To customize example ,use:\n" +
                " WindowJoin [--windowSize <window-size-in-millis>] [--rate <elements-per-second>]");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);

        env.getConfig().setGlobalJobParameters(params);

        DataStream<Tuple2<String, Integer>> grades = WindowJoinSampleData.GradeSource.getSource(env, rate);
        grades.print("xxxxxxxxxxxxxxxx");
        DataStream<Tuple2<String, Integer>> salaries = WindowJoinSampleData.SalarySource.getSource(env, rate);
        salaries.print("yyyyyyyyyyyy");
        DataStream<Tuple3<String, Integer, Integer>> joinedStream = grades
                .join(salaries)
                .where(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .equalTo(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.milliseconds(windowSize))).apply(new JoinFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, Tuple3<String, Integer, Integer>>() {
                    @Override
                    public Tuple3<String, Integer, Integer> join(
                            Tuple2<String, Integer> first,
                            Tuple2<String, Integer> second) throws Exception {
                        return new Tuple3<>(first.f0, first.f1, second.f1);
                    }
                });

        joinedStream.print().setParallelism(1);

        try {

            env.execute("Windowed Join Example");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
