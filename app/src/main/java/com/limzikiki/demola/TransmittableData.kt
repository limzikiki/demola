package com.limzikiki.demola

import com.google.gson.Gson

data class TransmittableData (
   var batteryLevel: Int = -1,
   var altitude: Float = -1f,
   var flightTime: Int = -1
) {
   override fun toString(): String {
      return Gson().toJson(this)
   }
}

