package org.chiflink.ctaprocessor.processors.ctaprocessor.models

/**
  * Created by ubuntu on 2/21/17.
  */
class Fix(val vid:Int,
          val tmstmp:String,
          val lat:Float,
          val lon:Float,
          val hdg:Int,
          val pid:Int,
          val rt:String,
          val des:String,
          val pdist:Int,
          val tablockid:String,
          val tatripid:Int) {

  override def toString(): String = {
    "vid:" + vid +" tmstmp:" +tmstmp + " lat:"+lat
  }
}
