package com.example.telekotlin.ui


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentAudioRecordBinding
import java.io.IOException


private const val TAG = "AudioRecordFragment"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AudioRecordFragment : Fragment() {


    private var fileName: String = ""

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private lateinit var binding: FragmentAudioRecordBinding


    private var timeInMilliseconds: Long = 0L
    private var startHTime: Long = 0L
    private val timeSwapBuff: Long = 0L
    private val customHandler: Handler = Handler()


    private var isRecording: Boolean = false
    private var isPlaying: Boolean = false


    private val runnable = object : Runnable {
        override fun run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime
            val updatedTime: Long = timeSwapBuff + timeInMilliseconds

            var secs: Int = (updatedTime / 100).toInt()
            val mins: Int = secs / 60
            secs %= 60


            binding.tvTimer.text =
                "" + String.format("%02d", mins) + ":" + String.format("%02d", secs)


            customHandler.postDelayed(this, 0)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAudioRecordBinding.inflate(inflater, container, false)

        fileName = "${activity!!.externalCacheDir!!.absolutePath}/audiorecordtest.3gp"

        ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_RECORD_AUDIO_PERMISSION)


        binding.startRecordBtn.setOnClickListener {


        }

        binding.stopRecordBtn.setOnClickListener {
        }

        binding.startPlayingBtn.setOnClickListener {
            onPlay(true)
        }







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
                binding.recordBtn.setImageDrawable(activity!!.resources.getDrawable(R.drawable.ic_record_grey600_48dp))
                isRecording = false
            }

        }


        binding.playBtn.setOnClickListener {

        }



        return binding.root
    }


    private fun startRecording() {
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


}
