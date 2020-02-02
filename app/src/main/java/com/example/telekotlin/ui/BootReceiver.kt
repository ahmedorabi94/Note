package com.example.telekotlin.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val myIntent = Intent(context, MyService::class.java)
        context!!.startService(myIntent)
    }

}