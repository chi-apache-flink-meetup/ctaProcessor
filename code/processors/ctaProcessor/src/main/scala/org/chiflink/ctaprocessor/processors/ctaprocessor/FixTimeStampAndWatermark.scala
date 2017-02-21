package org.chiflink.ctaprocessor.processors.ctaprocessor

/**
  * Created by ubuntu on 2/21/17.
  */
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.chiflink.ctaprocessor.processors.ctaprocessor.models.Fix
import org.apache.flink.streaming.api.watermark.Watermark
import scala.math.max

class FixTimeStampAndWatermark extends AssignerWithPeriodicWatermarks[Fix]{

  val maxOutOfOrderness = 1000L *60 * 10; // 10 minutes

  var currentMaxTimestamp:Long = 0;

  override def extractTimestamp(element: Fix, previousElementTimestamp: Long): Long = {
    //"20170214 17:59"
    val format = new java.text.SimpleDateFormat("yyyyMMdd HH:mm")
    val newTime = format.parse(element.tmstmp).getTime
    currentMaxTimestamp = max(newTime, currentMaxTimestamp)
    newTime
  }

  override def getCurrentWatermark(): Watermark = {
    // return the watermark as current time minus the maximum time lag
    //println("Watermark: "+(System.currentTimeMillis() - maxTimeLag))
    new Watermark(currentMaxTimestamp - maxOutOfOrderness);
  }
}
