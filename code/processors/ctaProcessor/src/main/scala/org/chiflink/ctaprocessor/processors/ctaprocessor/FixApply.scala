package org.chiflink.ctaprocessor.processors.ctaprocessor

import org.apache.flink.streaming.api.scala.function.RichWindowFunction
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector
import org.chiflink.ctaprocessor.processors.ctaprocessor.models.Fix

/**
  * Created by ubuntu on 2/21/17.
  */
class FixApply extends RichWindowFunction[Fix, Fix, Int, GlobalWindow] {

  override def apply(key: Int, window: GlobalWindow, inputFixes: Iterable[Fix], out: Collector[Fix]): Unit = {
    //Do something here
    var a:Fix = null
    for (in <- inputFixes) {
      a = in
    }
    out.collect(a)
  }
}
