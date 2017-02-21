package org.chiflink.ctaprocessor.loaders.ctaloader

/**
  * Created by ubuntu on 2/20/17.
  */
import com.beust.jcommander.JCommander
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010
import java.util.Properties
import org.apache.flink.streaming.util.serialization.SimpleStringSchema


object LoadFixes {

  val config = new LoadFixesArgs

  def main(args: Array[String]): Unit = {

    new JCommander(this.config, args: _*)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.getConfig.setGlobalJobParameters(this.config)

    val kafkaProps = new Properties
    kafkaProps.setProperty("zookeeper.connect", this.config.kafkaZookeeperHost)
    kafkaProps.setProperty("bootstrap.servers", this.config.kafkaBootStrapServer)


    val stream = env.readTextFile("file://"+this.config.inputFile).name("CTAStream")
        .flatMap(new FixProcessor).name("FixProcessor")
        .addSink(new FlinkKafkaProducer010[String](this.config.kafkaTopic,new SimpleStringSchema(), kafkaProps))


    env.execute("ctaLoader")
  }
}
