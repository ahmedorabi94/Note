package com.example.telekotlin.repository

import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.repository.roomDb.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SignatureRepository @Inject constructor(private val dao: NoteDao) {


    fun insertNote(title: String, body: String, signImage: ByteArray?) {

        val note = Note(title, body, signImage)

        CoroutineScope(IO).launch {
            dao.insert(note)
        }
    }




}