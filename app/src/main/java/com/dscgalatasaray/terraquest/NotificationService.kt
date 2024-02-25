package com.dscgalatasaray.terraquest

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.util.Calendar

const val NOTIFICATION_CHANNEL_ID = "ch-1"
const val NOTIFICATION_CHANNEL_NAME = "Reminders"
const val NOTIFICATION_ID = 100
const val REQUEST_CODE = 200

class NotificationService (
    private val context: Context
){
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val myIntent = Intent(context, MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        myIntent,
        PendingIntent.FLAG_IMMUTABLE
    )
    open fun showNotification(){
        val notification =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("TerraQuest")
                .setContentText("Değişecek Text")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager.notify(NOTIFICATION_ID,notification)
    }
}