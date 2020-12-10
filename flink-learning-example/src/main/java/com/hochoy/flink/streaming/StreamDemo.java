package com.hochoy.flink.streaming;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class StreamDemo {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        List<Tuple2> list = new ArrayList<>();
        list.add(Tuple2.of(1,1));
        list.add(Tuple2.of(1,1));
        list.add(Tuple2.of(1,1));
        list.add(Tuple2.of(1,1));
        list.add(Tuple2.of(1,1));

    }
}
