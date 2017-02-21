package org.chiflink.ctaprocessor.processors.ctaprocessor

/**
  * Created by ubuntu on 2/21/17.
  */

import java.util.Properties

import com.beust.jcommander.JCommander
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaProducer010}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
import org.chiflink.ctaprocessor.processors.ctaprocessor.models.{Fix, FixTrigger}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

object ProcessFixes {

  val config = new ProcessFixesArgs

  def main(args: Array[String]): Unit = {

    new JCommander(this.config, args: _*)
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.getConfig.setGlobalJobParameters(this.config)

    val kafkaProps = new Properties
    kafkaProps.setProperty("zookeeper.connect", this.config.kafkaZookeeperHost)
    kafkaProps.setProperty("bootstrap.servers", this.config.kafkaBootStrapServer)
    kafkaProps.setProperty("auto.offset.reset", "earliest")

    val consumer = new FlinkKafkaConsumer010[String](
      this.config.kafkaTopic,
      new SimpleStringSchema,
      kafkaProps)

    val stream = env.addSource(consumer).name("CTAFixStream")
       .map(new FixProcessor())
      //.flatMap(new FixProcessor()).name("CTAFixProcessor")
      .assignTimestampsAndWatermarks(new FixTimeStampAndWatermark())
      .keyBy(x=>(x.vid))
      .window(GlobalWindows.create())
      .trigger(new FixTrigger())
      .apply(new FixApply())

    env.execute("CTAFixStream")

  }

}
