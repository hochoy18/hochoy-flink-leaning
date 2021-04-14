package com.hochoy.flink.streaming.window

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

object CountWindowAll {


  def main(args: Array[String]): Unit = {
    // 1
    // 2
    // 3
    // 4
    // 5
    // 6
    // 7
    // 8
    // 9
    // 10
    // 1
    // 2
    // 3
    // 4
    // 5
    // 6
    // 7
    // 8
    // 9
    // 10

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source: DataStream[String] = env.socketTextStream("localhost", 9999)

    val window: AllWindowedStream[Int, GlobalWindow] = source.map(_.toInt).countWindowAll(10)

    val sum: DataStream[Int] = window.sum(0)

    sum.print()

    env.execute()

  }
}


object CountWindowAll2 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source: DataStream[String] = env.socketTextStream("localhost", 9999)

    val window: AllWindowedStream[Int, GlobalWindow] = source.map(_.toInt).countWindowAll(10, 5)

    val sum: DataStream[Int] = window.sum(0)

    sum.print()


    env.execute()
  }
}


object CountWindow {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source: DataStream[String] = env.socketTextStream("localhost", 9999)

    val map: DataStream[(String, Int)] = source.map(e => {
      val split: Array[String] = e.split(",")
      (split(0), split(1).toInt)
    })

    val keyby: KeyedStream[(String, Int), String] = map.keyBy(_._1)

    val window: WindowedStream[(String, Int), String, GlobalWindow] = keyby.countWindow(10)

    val sum: DataStream[(String, Int)] = window.sum(1)

    sum.print()

    env.execute()

  }
}


object CountWindow2{
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source: DataStream[String] = env.socketTextStream("localhost", 9999)

    val map: DataStream[(String, Int)] = source.map(e => {
      val split: Array[String] = e.split(",")
      (split(0), split(1).toInt)
    })

    val keyby: KeyedStream[(String, Int), String] = map.keyBy(_._1)

    val window: WindowedStream[(String, Int), String, GlobalWindow] = keyby.countWindow(4, 2)

    val sum: DataStream[(String, Int)] =
      window.sum(1)

    sum.print()

    env.execute()


  }


}