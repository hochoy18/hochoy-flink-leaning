package com.hochoy.flink.streaming.transform

import com.hochoy.flink.sink.Constants
import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

import java.util.Properties

object KafkaWindowWordCount {

  def main(args: Array[String]): Unit = {


    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val props = new Properties();
    props.put("bootstrap.servers", "localhost:9092")
    props.put("group.id", "flink-kafka-demo")

    val source: DataStream[String] = env.addSource(new FlinkKafkaConsumer011[String](Constants.topic, new SimpleStringSchema(), props))

//    val windowCounts =
//      source.filter(StringUtils.isNoneBlank(_))
//        .flatMap { w => w.split("\\s") }
//        .filter(StringUtils.isNoneBlank(_))
//      .map { w =>  (w, 1) }
//      .keyBy(0)
//      .timeWindow(Time.seconds(5))
//      .sum(1)
//    windowCounts.print(" >>>>>>> ")

    val summed: DataStream[(String,Int)] =
      source
        .filter(e => StringUtils.isNotBlank(e))
        .flatMap(_.split("\\s"))
        .filter(StringUtils.isNotBlank(_)).map((_, 1))
        .keyBy(0)
        .timeWindow(Time.seconds(5))
        .sum(1)

    summed.print("sum>>>   ")
    env.execute()
  }
  case class WordWithCount(word: String, count: Long)
}
