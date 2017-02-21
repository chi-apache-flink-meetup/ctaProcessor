package org.chiflink.ctaprocessor.processors.ctaprocessor

/**
  * Created by ubuntu on 2/21/17.
  */
import org.chiflink.ctaprocessor.processors.ctaprocessor.models.Fix
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.util.Collector
import org.json4s._
import org.json4s.native.JsonMethods._

class FixProcessor extends RichFlatMapFunction[String, Fix] {
  override def flatMap(value: String, out: Collector[Fix]): Unit = {
    implicit val formats = DefaultFormats
    val new1 = parse(value).extract[Fix]
    println(new1.toString())
    out.collect(new1)
  }
}
