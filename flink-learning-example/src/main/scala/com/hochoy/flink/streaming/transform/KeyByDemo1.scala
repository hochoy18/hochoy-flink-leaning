package com.hochoy.flink.streaming.transform

import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

import java.util.Properties

//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

class KeyByDemo1 {

}
object KeyByDemo1{
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment;

    val topic = "hochoy_flink";
    val props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "flink-kafka-demo");

    val source: DataStream[String] = env.addSource(new FlinkKafkaConsumer011[String](topic, new SimpleStringSchema(), props))
    source.print("source >>>>>")

    val flatMap: DataStream[String] = source.filter(StringUtils.isNotBlank(_)).flatMap(_.split("\\s"))
    val wordAndOne: DataStream[(String, Int)] = flatMap.map((_, 1))

    wordAndOne.print("wordAndOne >>> ")
    val keyed: KeyedStream[(String, Int), Tuple] = wordAndOne.keyBy(0)
    keyed.print("keyed >>> ")
    val summed: DataStream[(String, Int)] = keyed.sum(1)
    summed.print("summed >>> ")

    println
    env.execute("KeyByDemo1")
  }
}
