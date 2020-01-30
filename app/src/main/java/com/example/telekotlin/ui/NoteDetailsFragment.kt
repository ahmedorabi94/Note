package com.example.telekotlin.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentNoteDetailsBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.viewModels.DetailViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class NoteDetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailViewModel

    private lateinit var binding: FragmentNoteDetailsBinding

    private var rowId: Int = -1
    private var body: String? = ""

    private var isSign: Boolean = false
    private var fileName: String? = null
    private var player: MediaPlayer? = null
    private var time: String =""

    private val customHandler: Handler = Handler()

    private var dateStr: String = ""

//    private val runnable = object : Runnable {
//
//        override fun run() {
//            if (player != null) {
//                val currentPosition = player!!.currentPosition
//                binding.seekbar.progress = currentPosition
//            }
//            customHandler.postDelayed(this, 50)
//        }
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg = arguments
        if (arg != null) {
            rowId = arg.getInt("row_id")
            isSign = arg.getBoolean("isSign")
            body = arg.getString("body")
            fileName = arg.getString("fileName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)


        if (!isSign) {
            binding.signImage.visibility = View.GONE
        }

        setHasOptionsMenu(true)

        if (rowId != -1) {

            activity!!.title = "Edit Note"

            viewModel.getNote(rowId)

            if (body != null) {
                binding.edBody.setText(body)
            } else {
                viewModel.getNoteLiveData().observe(this.viewLifecycleOwner, Observer {
                    Log.e("NoteDetail", it.body)

                    binding.edTitle.setText(it.title)
                    binding.edBody.setText(it.body)
                    binding.tvTime.text = it.time
                    binding.tvDate.text = it.date

                    if (isSign) {
                        val bitmap =
                            BitmapFactory.decodeByteArray(it.signature, 0, it.signature!!.size)
                        binding.signImage.setImageBitmap(bitmap)
                    }
                })
            }


        } else {
            activity!!.title = "Add Note"


        }


        binding.saveNoteFab.setOnClickListener {
            saveNote(binding.edTitle.text.toString(), binding.edBody.text.toString())
            Navigation.findNavController(binding.root).navigateUp()
        }

//        if (fileName != null) {
//            initMediaPlayer(fileName!!)
//            binding.seekbar.max = player!!.duration
//
//        }


//        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (player != null) {
//                    player!!.seekTo(progress)
//                }
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//            }
//
//        })


//        binding.playBtn.setOnClickListener {
//            if (player != null){
//                player!!.start()
//                customHandler.postDelayed(runnable, 1000)
//            }
//
//        }

        val calender = Calendar.getInstance()

        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(calender)
            }


        binding.dateImage.setOnClickListener {
            DatePickerDialog(
                activity!!, datePickerOnDataSetListener, calender
                    .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.timeImage.setOnClickListener {

            val myCalendar = Calendar.getInstance()
            val timeSetLisener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                myCalendar.set(Calendar.MINUTE, minute)
                updateTime(myCalendar)

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


    private fun updateTime(myCalendar: Calendar) {
        time = SimpleDateFormat("HH:mm", Locale.UK).format(myCalendar.time)
        binding.tvTime.text = SimpleDateFormat("HH:mm a", Locale.UK).format(myCalendar.time)
        Log.e("Reminder", time)
    }

    private fun initMediaPlayer(fileName: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()

            } catch (e: IOException) {
                Log.e("ListItem", "prepare() failed")

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.add_text_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_current_item -> {
                viewModel.deleteNote(rowId)
                Navigation.findNavController(binding.root).navigateUp()
                return true
            }

            R.id.save_btn -> {
                saveNote(binding.edTitle.text.toString(), binding.edBody.text.toString())
                Navigation.findNavController(binding.root).navigateUp()

                return true
            }
        }



        return super.onOptionsItemSelected(item)

    }


    private fun saveNote(title: String, body: String) {
        if (rowId == -1 || this.body != null) {
            viewModel.insertNewNote(title, body, dateStr,time)

        } else {
            viewModel.updateNote(rowId, title, body)
        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val myFormat2 = "dd MMMM yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        val sdf2 = SimpleDateFormat(myFormat2, Locale.UK)

        dateStr = sdf.format(myCalendar.time)

        binding.tvDate.text = sdf2.format(myCalendar.time)

        Log.e("Reminder", dateStr)
    }


}
