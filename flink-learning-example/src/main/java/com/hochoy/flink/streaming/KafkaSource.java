package com.hochoy.flink.streaming;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.nio.charset.Charset;
import java.util.Properties;

public class KafkaSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =  StreamExecutionEnvironment.getExecutionEnvironment();

        String topic  = "";
        Properties props = new Properties();
        props.put("bootstrap.servers","172.172.177.70:9092,172.172.177.72:9092,172.172.177.73:9092");
//        props.put("zookeeper.connect", parameterTool.get("");
        props.put("group.id","k_interface_call");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest");



//
//        FlinkKafkaConsumer010  consumer = new FlinkKafkaConsumer010<>(
//                topic,
//                new SimpleStringSchema(Charset.forName("UTF-8")),
//                props);


        env.execute("flink learning connectors kafka");
    }

}
