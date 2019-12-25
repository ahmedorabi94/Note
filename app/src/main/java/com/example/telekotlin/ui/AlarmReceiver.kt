package com.example.telekotlin.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action != null && context != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {

                NotificationScheduler.setReminder(context, 4, 3, AlarmReceiver::class.java)

                return
            }

        }

        NotificationScheduler.showNotification(
            context!!,
            MainActivity::class.java,
            "You have 5 unwatched videos",
            "Watch them now?"
        )
    }


}