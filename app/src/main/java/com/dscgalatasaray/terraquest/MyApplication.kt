package com.dscgalatasaray.terraquest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel

const val ONESIGNAL_APP_ID = "a0581cc2-941e-48c5-bb82-72ac7f04167e"

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }

    }
}