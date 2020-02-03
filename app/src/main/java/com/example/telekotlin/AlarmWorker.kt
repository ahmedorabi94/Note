package com.example.telekotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.telekotlin.ui.AlarmReceiver
import java.util.*

class AlarmWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


    override fun doWork(): Result {

        val timeData = inputData.getStringArray("timeData")

        // val timeData : Array< out String>? = inputData.getStringArray("timeData")

        for (item in timeData!!.asList()) {
            Log.e("AlarmWorker", item)

            val list: List<String> = item.split(":")
            val hour = list[0]
            val min = list[1]

            setAlarm(hour.toInt(), min.toInt())

        }

        return Result.success()
    }


    private fun setAlarm(hour: Int, min: Int) {
        val myIntent = Intent(applicationContext, AlarmReceiver::class.java)
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, id, myIntent, 0)
        val calendar = Calendar.getInstance()
        //   calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        //    calendar.set(Calendar.AM_PM, Calendar.AM)
        //    calendar.add(Calendar.DAY_OF_MONTH, 1)


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


}