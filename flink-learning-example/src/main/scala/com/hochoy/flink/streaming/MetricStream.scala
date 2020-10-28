package com.hochoy.flink.streaming

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import com.hochoy.flink.sink.JdbcSink
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.datastream.{DataStream, DataStreamSource, SingleOutputStreamOperator}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}

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

    val value: DataStream[JSONObject] = source.map(new RichMapFunction[String, JSONObject] {
      override def map(value: String): JSONObject = {
        JSON.parseObject(value)
      }
    })

    value.addSink(new JdbcSink[JSONObject])
    val sink2: DataStream[String] = value.map(new RichMapFunction[JSONObject, String] {
      override def map(value: JSONObject): String = value.toJSONString
    })
    sink2.addSink(new FlinkKafkaProducer011[String](bootstrapServer, sourceTopic,new SimpleStringSchema))

    env.execute(this.getClass.getName)
  }
}
