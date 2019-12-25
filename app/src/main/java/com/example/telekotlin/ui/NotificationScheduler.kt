package com.example.telekotlin.ui

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.telekotlin.R
import java.util.*


class NotificationScheduler {


    companion object {

        val DAILY_REMINDER_REQUEST_CODE = 100

        fun setReminder(context: Context, hour: Int, min: Int, cls: Class<AlarmReceiver>) {


            val calender = Calendar.getInstance()
            val setCalender = Calendar.getInstance()

            setCalender.set(Calendar.HOUR_OF_DAY, hour)
            setCalender.set(Calendar.MINUTE, min)
            setCalender.set(Calendar.SECOND, 0)


            if (setCalender.before(calender)){
                setCalender.add(Calendar.DATE,1)
            }


            val receiver = ComponentName(context, cls)
            val pm = context.packageManager
            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            val intent = Intent(context, cls)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    DAILY_REMINDER_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, setCalender.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent
            )

        }

        fun showNotification(
            context: Context,
            cls: Class<MainActivity>,
            title: String,
            content: String
        ) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationIntent = Intent(context, cls)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(cls)
            stackBuilder.addNextIntent(notificationIntent)

            val pendingIntent =
                stackBuilder.getPendingIntent(
                    DAILY_REMINDER_REQUEST_CODE,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)

            val notification = builder.setContentTitle(title)
                .setContentText(content).setAutoCancel(true)
                .setSound(uri).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent).build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification)

        }


    }


}