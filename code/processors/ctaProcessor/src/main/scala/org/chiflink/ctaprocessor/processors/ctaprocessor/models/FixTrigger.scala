package org.chiflink.ctaprocessor.processors.ctaprocessor.models

/**
  * Created by ubuntu on 2/21/17.
  */
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.scala.function.util.ScalaReduceFunction
import org.apache.flink.streaming.api.windowing.triggers.Trigger.TriggerContext
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.Window


class FixTrigger[E]() extends Trigger[Fix, Window] {

  val countState = new ReducingStateDescriptor[Long]("sessionCount", new ScalaReduceFunction[Long](_ + _), classOf[Long])
  val timeState = new ValueStateDescriptor[Option[Long]]("sessionTimer", classOf[Option[Long]], None)
  val timeout = 180
  val maxNum = 1000

  override def onElement(t: Fix, l: Long, w: Window, triggerContext: TriggerContext): TriggerResult = {

    // remove old timer
    val time_state: ValueState[Option[Long]] = triggerContext.getPartitionedState(timeState)
    val time_set = time_state.value()
    if (time_set.isDefined) {
      triggerContext.deleteProcessingTimeTimer(time_set.get)
    }

    // update count and check if over limit
    val state: ReducingState[Long] = triggerContext.getPartitionedState(countState)
    state.add(1)
    val ct = state.get()
    println("Items in window ("+ct +")")
    //println("   Adding: " + t.toString)
    if (ct >= maxNum) {
      println("   Triggered by count. Num Items in window: "+ ct + " Max num allowed: "+maxNum)
      time_state.update(None)
      TriggerResult.FIRE_AND_PURGE
    } else {
      // set new time and continue
      val new_time = triggerContext.getCurrentProcessingTime + (1000 * timeout)
      //println("   Resetting timer to: "+new_time+" using value: "+t.getValue)
      time_state.update(Some(new_time))
      triggerContext.registerProcessingTimeTimer(new_time)
      TriggerResult.CONTINUE
    }
  }

  override def clear(window: Window, ctx: TriggerContext): Unit = {
    val state: ReducingState[Long] = ctx.getPartitionedState(countState)
    state.clear()
  }


  override def onProcessingTime(l: Long, w: Window, triggerContext: TriggerContext): TriggerResult = {
    println("   Triggered by (processing) timeout at: "+l)
    TriggerResult.FIRE_AND_PURGE
  }

  override def onEventTime(l: Long, w: Window, triggerContext: TriggerContext): TriggerResult = {
    println("   Triggered by (event) timeout")
    TriggerResult.CONTINUE
  }

}
