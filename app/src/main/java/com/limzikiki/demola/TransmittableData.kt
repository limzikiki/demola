package com.limzikiki.demola

import com.google.gson.Gson

data class TransmittableData (
   var batteryLevel: Int = 0,
   var batteryLifetimeRemaining: Int = 0
) {
   override fun toString(): String {
      return Gson().toJson(this)
   }
}

