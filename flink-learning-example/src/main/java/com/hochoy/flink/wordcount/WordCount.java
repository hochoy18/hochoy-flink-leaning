package com.hochoy.flink.wordcount;

import com.hochoy.flink.wordcount.util.WordCountData;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.Arrays;

public class WordCount {
    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.getConfig().setGlobalJobParameters(params);

        DataStream<String> text;
        if (params.has("input")){
            text = env.readTextFile(params.get("input"));
        }else {
            text = env.fromElements(WordCountData.WORDS);
        }

        DataStream<Tuple2<String, Long>> word_1 = text.flatMap(new Tokenizer());
//        word_1.print();

        KeyedStream<Tuple2<String, Long>, Tuple> keyedStream = word_1.keyBy(0);
//        keyedStream.print();
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = keyedStream.sum(1);

        if(params.has("output")){
            sum.print();
            sum.setParallelism(1).writeAsText(params.get("output"), FileSystem.WriteMode.OVERWRITE);
        }else
        {
            sum.print();
        }
        try {

            env.execute("WordCount");
        }catch (Exception e ){
            e.printStackTrace();
        }
    }


    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String,Long>>{
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
            Arrays.asList(value.split("\\W+")).
                    forEach(x->out.collect(new Tuple2<>(x,1L)));
        }
    }
}
