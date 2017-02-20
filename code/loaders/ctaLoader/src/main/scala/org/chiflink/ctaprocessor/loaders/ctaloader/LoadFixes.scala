package org.chiflink.ctaprocessor.loaders.ctaloader

/**
  * Created by ubuntu on 2/20/17.
  */
import com.beust.jcommander.JCommander
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.api.scala._


object LoadFixes {

  val config = new LoadFixesArgs

  def main(args: Array[String]): Unit = {
    new JCommander(this.config, args: _*)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.getConfig.setGlobalJobParameters(this.config)


  }
}
