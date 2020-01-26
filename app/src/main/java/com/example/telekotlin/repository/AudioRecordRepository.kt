package com.example.telekotlin.repository

import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.repository.roomDb.NoteDao
import com.example.telekotlin.util.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioRecordRepository @Inject constructor(private val noteDao: NoteDao) {


    fun insertNote(title: String, body: String, fileName: String) {
        val note = Note(title, body, null, AppUtils.getDate(), fileName,"",false)

        CoroutineScope(IO).launch {
            noteDao.insert(note)
        }

    }

}