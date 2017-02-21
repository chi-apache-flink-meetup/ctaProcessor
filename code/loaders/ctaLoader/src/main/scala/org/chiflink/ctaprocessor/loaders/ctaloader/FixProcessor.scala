package org.chiflink.ctaprocessor.loaders.ctaloader

/**
  * Created by ubuntu on 2/20/17.
  */
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.util.Collector
import scala.util.parsing.json._

class FixProcessor extends RichFlatMapFunction[String, String] {

  override def flatMap(in: String, out: Collector[String]): Unit = {

    val parsed = JSON.parseFull(in.dropRight(1))
    if (!parsed.isEmpty) {
      out.collect(in)
    }
    else {
      println("JSON Error: "+in.dropRight(1))
    }
  }
}

