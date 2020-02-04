package com.example.telekotlin.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.telekotlin.R


private const val default_notification_channel_id: String = "default"
private const val NOTIFICATION_CHANNEL_ID: String = "10001"

class AlarmReceiver : BroadcastReceiver() {

    private var title: String = ""


    override fun onReceive(context: Context?, intent: Intent?) {
        title = intent!!.extras!!.getString("taskTitle")!!
        showNotification(context ,title)


    }


    private fun showNotification(context: Context? , title: String) {

        val notificationIntent = Intent(context, MainActivity::class.java)


        notificationIntent.putExtra("fromNotification", true)
        notificationIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)


        val myNotificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val mBuilder =
            NotificationCompat.Builder(context, default_notification_channel_id)
        mBuilder.setContentTitle("To Do Now")
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setContentText(title)
        mBuilder.setSmallIcon(R.drawable.ic_alarm_grey600_18dp)
        mBuilder.setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            myNotificationManager.createNotificationChannel(notificationChannel)
        }


        myNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())

    }

}