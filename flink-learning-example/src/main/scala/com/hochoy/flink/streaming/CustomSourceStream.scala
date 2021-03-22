package com.hochoy.flink.streaming

import com.hochoy.common.EnumType
import com.hochoy.flink.sources.CustomStreamingSource
import org.apache.flink.streaming.api.scala._



object CustomSourceStream {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val source: DataStream[String] = env.addSource(
      new CustomStreamingSource(EnumType.SourceEnum.word_count) )
      .setParallelism(1)


    val flat: DataStream[(String, Int)] = source
      .flatMap(w => w.split("\\s")).setParallelism(2)
      .map((_, 1))

    flat.print("flatMap====>>>>>    ")

    val summed = flat.keyBy(0).sum(1)

    summed.print("keyBy and sum ====>    ")


    val wordCounts: DataStream[WordCount] = source.flatMap(e => e.split("\\s"))
      .map( WordCount(_, 1))
    wordCounts.print("WordCount           >    ")

    val count: DataStream[WordCount] = wordCounts.keyBy("word").sum("count")

    count.print("count .......... _ ")


//    source.print("streaming sink >>>>>>>>>>>")
    env.execute(this.getClass.getName)
  }

  case   class WordCount(word:String , count : Int)

}
