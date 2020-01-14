package com.example.telekotlin.ui


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentAudioRecordBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.viewModels.AudioRecordViewModel
import java.io.IOException
import javax.inject.Inject


private const val TAG = "AudioRecordFragment"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AudioRecordFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AudioRecordViewModel

    private var fileName: String? = null

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private lateinit var binding: FragmentAudioRecordBinding


    private var timeInMilliseconds: Long = 0L
    private var startHTime: Long = 0L
    private var timeSwapBuff: Long = 0L
    private val customHandler: Handler = Handler()

    private var isRecording: Boolean = false
    private var isPlaying: Boolean = false
    private var i: Int = 0


    private val runnable = object : Runnable {
        override fun run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime
            val updatedTime = timeSwapBuff + timeInMilliseconds

            var secs: Int = (updatedTime / 100).toInt()
            val mins: Int = secs / 60
            secs %= 60


            binding.tvTimer.text = String.format("%02d", mins) + ":" + String.format("%02d", secs)

            customHandler.postDelayed(this, 0)
        }

    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAudioRecordBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AudioRecordViewModel::class.java)

        //  fileName = "${activity!!.externalCacheDir!!.absolutePath}/audiorecordtest.3gp"

        setHasOptionsMenu(true)

        ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_RECORD_AUDIO_PERMISSION)


        binding.recordBtn.setOnClickListener {

            if (!isRecording) {
                startHTime = SystemClock.uptimeMillis()
                customHandler.postDelayed(runnable, 0)
                onRecord(true)
                binding.recordBtn.setImageDrawable(activity!!.resources.getDrawable(R.drawable.ic_pause_grey600_48dp))
                isRecording = true
            } else {
                // pause not stop
                onRecord(false)
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(runnable);
                timeSwapBuff = 0L;
                startHTime = 0L;
                binding.recordBtn.setImageDrawable(activity!!.resources.getDrawable(R.drawable.ic_record_grey600_48dp))
                isRecording = false
            }

        }

        binding.playBtn.setOnClickListener {
            onPlay(true)
        }


        return binding.root
    }


    private fun startRecording() {

        i++
        fileName = "${Environment.getExternalStorageDirectory().absolutePath}/sound/test${i}.3gp"


        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")

            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }


    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")

            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }


    private fun onRecord(start: Boolean) {
        if (start) {
            startRecording()
        } else {
            stopRecording()
        }
    }


    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if (!permissionToRecordAccepted) activity?.finish()
    }


    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.add_text_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_current_item -> {
//                viewModel.deleteNote(rowId)
//                Navigation.findNavController(binding.root).navigateUp()
                return true
            }

            R.id.save_btn -> {

                if (fileName != null) {
                    viewModel.insertNote(
                        binding.edTitle.text.toString(),
                        binding.edBody.text.toString(),
                        fileName!!
                    )
                    Navigation.findNavController(binding.root).navigateUp()

                } else {
                    Toast.makeText(activity, "Please Record Audio First", Toast.LENGTH_SHORT).show()
                }


                return true
            }
        }



        return super.onOptionsItemSelected(item)

    }


}
