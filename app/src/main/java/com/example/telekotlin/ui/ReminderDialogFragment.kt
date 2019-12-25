package com.example.telekotlin.ui


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.telekotlin.databinding.FragmentReminderDialogBinding
import java.text.SimpleDateFormat
import java.util.*


class ReminderDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentReminderDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentReminderDialogBinding.inflate(inflater, container, false)

        val calender = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(calender)
            }

        binding.alarmImage.setOnClickListener {

            DatePickerDialog(
                activity!!, datePickerOnDataSetListener, calender
                    .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }



        binding.timeImage.setOnClickListener {

            val calender = Calendar.getInstance()
            val timeSetLisener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calender.set(Calendar.HOUR_OF_DAY, hour)
                calender.set(Calendar.MINUTE, minute)

                Log.e("Reminder", SimpleDateFormat("HH:mm", Locale.UK).format(calender.time))


                NotificationScheduler.setReminder(
                    context!!,
                    hour,
                    minute,
                    AlarmReceiver::class.java
                )
            }

            TimePickerDialog(
                context,
                timeSetLisener,
                calender.get(Calendar.HOUR_OF_DAY),
                calender.get(Calendar.MINUTE),
                true
            ).show()

        }

        return binding.root
    }


    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        Log.e("Reminder", sdf.format(myCalendar.time))
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = 1000
        params.height = 1000
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

}
