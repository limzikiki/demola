package com.limzikiki.demola

import android.content.Context
import com.secneo.sdk.Helper

class Application: android.app.Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Helper.install(this@Application)
    }
}