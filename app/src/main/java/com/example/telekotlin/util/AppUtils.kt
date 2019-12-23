package com.example.telekotlin.util

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class AppUtils {


    companion object{
        fun getDate() : String{

            val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return sdf.format(Date())

        }
    }



}

