package com.example.telekotlin.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import java.util.*

class MyService : Service() {


    override fun onCreate() {
        super.onCreate()
        setAlarm()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun setAlarm() {
        val myIntent = Intent(this, AlarmReceiver::class.java)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 49)
        calendar.set(Calendar.HOUR, 16)
        calendar.set(Calendar.AM_PM, Calendar.AM)
        calendar.add(Calendar.DAY_OF_MONTH, 1)


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}