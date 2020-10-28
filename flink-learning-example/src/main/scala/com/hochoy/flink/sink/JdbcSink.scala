package com.hochoy.flink.sink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

import com.alibaba.fastjson.{JSON, JSONObject}
import com.hochoy.utils.HochoyUtils

class JdbcSink[String] extends RichSinkFunction[JSONObject] {


  var ps: PreparedStatement = _
  var connection: Connection = _

  override  def invoke(value: JSONObject, context: SinkFunction.Context[_]): Unit = {


   if (value != null){
     ps.setObject(1,value getDate  "start_time")
     ps.setObject(2,value getString "result_code")
     ps.setObject(3,value getString "type")
     ps.setObject(4, value getString "sign")
     ps.setObject(5, value getInteger  "result_code")
     ps.setObject(6, value getString "type")

     ps.execute()
   }

  }

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    val props = HochoyUtils.getProperties("jdbc.properties")
    try {
      Class.forName(props.getProperty("driver_class"))
      connection = DriverManager.getConnection(props.getProperty("jdbc_url"), props.getProperty("jdbc_user"), props.getProperty("jdbc_pwd"))
    } catch {
      case e: SQLException => e.printStackTrace()
    }
    ps = connection.prepareStatement(
      """
        |INSERT INTO gdm_fact_user_login_de ( k_dt, store_code, staff_id, cust_source, fuid, staff_name)
        |VALUES (?,?,?, ?,?,?)
      """.stripMargin)
  }

  override def close(): Unit = {
    super.close()
    if (ps != null) {
      ps.close()
    }
    if (connection != null) {
      connection.close()
    }
  }


}
object JdbcSink {
  def main(args: Array[String]): Unit = {
    val props = HochoyUtils.getProperties("jdbc.properties")


    val connection: Connection =    DriverManager.getConnection(props.getProperty("jdbc_url"), props.getProperty("jdbc_user"), props.getProperty("jdbc_pwd"))
    val ps: PreparedStatement =   connection.prepareStatement(
      """
        |INSERT INTO gdm_fact_user_login_de ( k_dt, store_code, staff_id, cust_source, fuid, staff_name)
        |VALUES (?,?,?, ?,?,?)
      """.stripMargin)
    try {
      Class.forName(props.getProperty("driver_class"))


      ps.setObject(1,"2020-10-28")
      ps.setObject(2, "111")
      ps.setObject(3,"19185407")
      ps.setObject(4, "8002")
      ps.setInt(5, 1)
      ps.setObject(6, "cai.he")

      ps.execute()
      ps.close()
      connection.close()
    } catch {
      case e: SQLException => e.printStackTrace()
    }

  }
}
