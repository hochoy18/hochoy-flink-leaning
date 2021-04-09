package com.hochoy.flink.streaming.transform

import com.hochoy.flink.sink.Constants
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

import java.util.Properties

object WindCount {


  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("group.id", "flink-kafka-demo")
    val env = StreamExecutionEnvironment.getExecutionEnvironment;

    val source: DataStream[String] =
      env.addSource(
      new FlinkKafkaConsumer011[String](Constants.topic, new SimpleStringSchema(), props)
      )

    val sum: DataStream[(String, Int)] = source.flatMap(_.split("\\s")).
      filter(_.nonEmpty).map((_, 1))
      .keyBy(_._1)
      .countWindow(5,3).sum(1)
    sum.print("sum >>>>   ")
    env.execute()

  }

}
