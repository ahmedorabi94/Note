package com.example.telekotlin.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.repository.roomDb.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val application: Application
) {


    private lateinit var noteLiveData: LiveData<Note>


    fun getNote(id: Int) {
        noteLiveData = noteDao.getTele(id)
    }


    fun insertNewNote(title: String, body: String) {

        val note = Note(title, body)
        CoroutineScope(IO).launch {
            noteDao.insert(note)
        }

    }

    fun deleteNote(id: Int) {
        CoroutineScope(IO).launch {
            noteDao.deleteTele(id)
        }
    }


    fun updateNote(id: Int, title: String, body: String) {

        CoroutineScope(IO).launch {
            noteDao.updateTele(id, title, body)
        }
    }


    fun getAsLiveData(): LiveData<Note> {
        return noteLiveData
    }

}