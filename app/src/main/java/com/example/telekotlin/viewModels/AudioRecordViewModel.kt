package com.example.telekotlin.viewModels

import androidx.lifecycle.ViewModel
import com.example.telekotlin.repository.AudioRecordRepository
import javax.inject.Inject

class AudioRecordViewModel @Inject constructor(private val repo: AudioRecordRepository) :
    ViewModel() {


    fun insertNote(title: String, body: String, fileName: String) {
        repo.insertNote(title, body, fileName)
    }

}