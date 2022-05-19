package com.limzikiki.demola

import android.content.Context
import com.secneo.sdk.Helper
import timber.log.Timber

class MApplication: android.app.Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Helper.install(this@MApplication)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}