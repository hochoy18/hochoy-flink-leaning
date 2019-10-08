//package com.hochoy.flink;
//
//import com.hochoy.flink.socket.SocketWindowWordCount;
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.api.java.utils.ParameterTool;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.util.Collector;
//import scala.Tuple2;
//
//import java.util.Arrays;
//
//public class WikipediaAnalysis {
//
//    public static void main(String[] args) {
//
//        Tuple2<String, Integer> hostAndPort = WikipediaAnalysis.ArgsOp.getHostAndPort(args);
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        DataStream<String> stream = env.socketTextStream(hostAndPort._1, hostAndPort._2, " ");
//
//        DataStream<SocketWindowWordCount.WordWithCount> words =
//                stream.flatMap(new FlatMapFunction<String, SocketWindowWordCount.WordWithCount>() {
//            @Override
//            public void flatMap(String value, Collector<SocketWindowWordCount.WordWithCount> out) throws Exception {
//                Arrays.asList(value.split("\\s"))
//                        .forEach(v -> out.collect(new SocketWindowWordCount.WordWithCount(v, 1L)));
//            }
//        });
//        DataStream<Tuple2<String, Long>> result = stream.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
//            @Override
//            public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
//                Arrays.asList(value.split("\\s")).forEach(v -> out.collect(new Tuple2<>(v, 1L)));
//            }
//        });
////        words.timeWindowAll()
////        tup2.timeWindowAll()
//
//
//    }
//
//    static class ArgsOp {
//        public static Tuple2<String, Integer> getHostAndPort(String[] args) {
//            final String hostname;
//            final int port;
//            try {
//                final ParameterTool params = ParameterTool.fromArgs(args);
//                hostname = params.has("hostname") ? params.get("hostname") : "localhost";
//                port = params.getInt("port");
//            } catch (Exception e) {
//                System.err.println("No port specified. Please run 'SocketWindowWordCount " +
//                        "--hostname <hostname> --port <port>', where hostname (localhost by default) " +
//                        "and port is the address of the text server");
//                System.err.println("To start a simple text server, run 'netcat -l <port>' and " +
//                        "type the input text into the command line");
//                return null;
//            }
//            return new Tuple2<String, Integer>(hostname, port);
//        }
//    }
//}
