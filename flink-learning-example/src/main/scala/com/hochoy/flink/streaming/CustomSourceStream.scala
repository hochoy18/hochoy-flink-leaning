package com.hochoy.flink.streaming

import com.hochoy.common.EnumType
import com.hochoy.flink.sources.CustomStreamingSource
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

object CustomSourceStream {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val source: DataStreamSource[String] = env.addSource(
      new CustomStreamingSource(EnumType.SourceEnum.word_count), "Custom Source")
      //.setParallelism(8)

    println(source.getParallelism)
    source.print("streaming sink >>>>>>>>>>>")
    env.execute(this.getClass.getName)
  }

}
