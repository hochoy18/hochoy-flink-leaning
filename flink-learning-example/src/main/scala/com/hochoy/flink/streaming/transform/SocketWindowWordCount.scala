package com.hochoy.flink.streaming.transform

import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import java.util.Date

object SocketWindowWordCount {

  var host :String = _
  var port :Int = _
  def main(args: Array[String]): Unit = {

    // --host localhost --port 9999

    val tool: ParameterTool = ParameterTool.fromArgs(args)

    host =  if (tool.has("host")) tool.get("host") else  "localhost"
    port = if(tool.has("port")) tool.getInt("port") else 9999

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val source: DataStream[String] =   env.socketTextStream(host, port)


//    val windowCounts =
//      source.filter(StringUtils.isNoneBlank(_))
//        .flatMap { w => w.split("\\s") }
//        .filter(StringUtils.isNoneBlank(_))
//      .map { w =>  (w, 1) }
//      .keyBy(0)
//      .timeWindow(Time.seconds(5))
//      .sum(1)
//    windowCounts.print(" >>>>>>> ")

    val split: DataStream[String] = source.filter(e => StringUtils.isNotBlank(e))
        .flatMap(_.split("\\s")).filter(StringUtils.isNotBlank(_))

    val wordAndOne: DataStream[(String, Int)] = split.map((_, 1))

    val summed: DataStream[(String, Int)] =
      wordAndOne
        .keyBy(0)
        .timeWindow(Time.seconds(5))
        .sum(1)
    summed.print("sum>>>   ")
    env.execute()
  }
  case class WordWithCount(word: String, count: Long)
}
