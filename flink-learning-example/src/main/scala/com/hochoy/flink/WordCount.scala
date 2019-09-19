package com.hochoy.flink

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.datastream.{DataStream, DataStreamSource}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011


object WordCount {
  private val WORDS: Array[String] = Array(
    "To be, or not to be,--that is the question:--",
    "Whether 'tis nobler in the mind to suffer",
    "The slings and arrows of outrageous fortune",
    "Or to take arms against a sea of troubles,",
    "And by opposing end them?--To die,--to sleep,--",
    "No more; and by a sleep to say we end",
    "The heartache, and the thousand natural shocks",
    "That flesh is heir to,--'tis a consummation",
    "Devoutly to be wish'd. To die,--to sleep;--",
    "To sleep! perchance to dream:--ay, there's the rub;",
    "For in that sleep of death what dreams may come,",
    "When we have shuffled off this mortal coil,",
    "Must give us pause: there's the respect",
    "That makes calamity of so long life;",
    "For who would bear the whips and scorns of time,",
    "The oppressor's wrong, the proud man's contumely,",
    "The pangs of despis'd love, the law's delay,",
    "The insolence of office, and the spurns",
    "That patient merit of the unworthy takes,",
    "When he himself might his quietus make",
    "With a bare bodkin? who would these fardels bear,",
    "To grunt and sweat under a weary life,",
    "But that the dread of something after death,--",
    "The undiscover'd country, from whose bourn",
    "No traveller returns,--puzzles the will,",
    "And makes us rather bear those ills we have",
    "Than fly to others that we know not of?",
    "Thus conscience does make cowards of us all;",
    "And thus the native hue of resolution",
    "Is sicklied o'er with the pale cast of thought;",
    "And enterprises of great pith and moment,",
    "With this regard, their currents turn awry,",
    "And lose the name of action.--Soft you now!",
    "The fair Ophelia!--Nymph, in thy orisons",
    "Be all my sins remember'd."
  )

  def main(args: Array[String]): Unit = {


    val params: ParameterTool = ParameterTool.fromArgs(args)
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setGlobalJobParameters(params)


    val topic:String = "action-topic"
    val deserializer: SimpleStringSchema = new SimpleStringSchema()
    val props:Properties = new Properties()
//    props.setProperty("bootstrap.servers", "xxxx:9092");
//    props.setProperty("group.id", "trafficwisdom-streaming");
//    props.put("enable.auto.commit", "false");
//    props.put("max.poll.records", "10001us")
//    val consumer = new FlinkKafkaConsumer011[String](topic,deserializer,props)
//    consumer.setStartFromLatest()
//    val stream: DataStream [String] = env.addSource(consumer)


    val text: DataStreamSource[String] = env.readTextFile("D:/advance/bigdata/flink/hochoy-flink-leaning/test_data/test1/test.txt")
//    text.flatMap(_.toLowerCase.split("\\W+") filter(_.nonEmpty)).map{ (_,1) }
//
//    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
//      .map { (_, 1) }
//      .groupBy(0)
//      .sum(1)
    println(text)
    text.print()


//    text.flatMap {}

  }
}
