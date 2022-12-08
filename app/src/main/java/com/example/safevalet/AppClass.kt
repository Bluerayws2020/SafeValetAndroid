package com.example.safevalet

import android.app.Application
import com.onesignal.OneSignal
import com.onesignal.OneSignalNotificationManager

private const val ONESIGNAL_APP_ID = "bac0fa74-ea86-42d1-8c85-ddff6d7f5ccc"

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}