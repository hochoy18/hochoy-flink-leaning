package com.hochoy.flink.sink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import java.sql.{Connection, PreparedStatement}
import java.sql.DriverManager

class JdbcSink[Any] extends RichSinkFunction {


  val ps: PreparedStatement = null
  var connection: Connection = null

  override def invoke(value: Nothing, context: SinkFunction.Context[_]): Unit = {
    super.invoke(value, context)
    var con = null
//    try {
//      Class.forName("com.mysql.jdbc.Driver")
//      con = DriverManager getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8", "root", "root123456")
//      DriverManager.getConnection("")
//    } catch {
//      case e: Exception =>
//        System.out.println("-----------mysql get connection has exception , msg = " + e.getMessage)
//    }

  }

  override def open(parameters: Configuration): Unit = super.open(parameters)

  override def close(): Unit = super.close()
}
