package com.example.telekotlin.ui


import android.app.DatePickerDialog
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

        return binding.root
    }


    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        Log.e("Reminder", sdf.format(myCalendar.time))
    }

}
