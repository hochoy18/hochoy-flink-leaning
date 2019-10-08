package com.hochoy.flink.twitter;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.StringTokenizer;

public class TwitterExample {

    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        env.getConfig().setGlobalJobParameters(params);
        
        env.setParallelism(params.getInt("parallelism",1));

        DataStream<String> stream ;//= env.fromElements(TwitterExampleData.TEXTS);
        
        if (!true){
            
        }else {
            stream = env.fromElements(TwitterExampleData.TEXTS);
        }

        DataStream<Tuple2<String, Long>> flatMap = stream.flatMap(new SelectEnglishAndTokenizeFlatMap());

        KeyedStream<Tuple2<String, Long>, Tuple> keyBy = flatMap.keyBy(0);
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = keyBy.sum(1);

        if (params.has("output")){
            sum.writeAsText(params.get("output"));
        }else {
            sum.print();
        }

        try {
            env.execute("twitter example");
        }catch (Exception e ){
            e.printStackTrace();
        }


    }
    public static class SelectEnglishAndTokenizeFlatMap
            implements FlatMapFunction<String,Tuple2<String,Long>>{

        private transient ObjectMapper jsonParser;


        @Override
        public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
            if (jsonParser == null){
                jsonParser = new ObjectMapper();
            }
            JsonNode jsonNode = jsonParser.readValue(value, JsonNode.class);
            boolean isEn = jsonNode.has("user") &&
                    jsonNode.get("user").has("lang") &&
                    jsonNode.get("user").get("lang").asText().equals("en");

            boolean hasText = jsonNode.has("text");

            if (isEn && hasText){
                StringTokenizer tokenizer = new StringTokenizer(jsonNode.get("text").asText());

                while (tokenizer.hasMoreElements()){
                    String result = tokenizer.nextToken().replaceAll("\\s*", "").toLowerCase();
                    if (!result.equals("")){
                        out.collect(new Tuple2(result,1L));
                    }
                }

            }

        }
    }

}
