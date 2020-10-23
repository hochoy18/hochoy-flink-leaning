package com.hochoy.flink.streaming

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object MetricStream {

  def main(args: Array[String]): Unit = {
    // --sourceTopic part3-topic  --bootstrap.servers localhost:9092
    // --sourceTopic data_collect_topic_trace  --bootstrap.servers 172.172.177.70:9092,172.172.177.72:9092,172.172.177.73:9092
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val tool: ParameterTool = ParameterTool.fromArgs(args)
    val sourceTopic = tool.get("sourceTopic")
    val bootstrapServer = tool.get("bootstrap.servers")
    val props = new Properties()
    props.setProperty("bootstrap.servers", bootstrapServer)
    props.setProperty("group.id", "kafka-demo")
    val source: DataStreamSource[String] = env.addSource(new FlinkKafkaConsumer011[String](sourceTopic,new SimpleStringSchema,props))
    println(source.getParallelism)
    source.print()
    env.execute(this.getClass.getName)
  }
}
