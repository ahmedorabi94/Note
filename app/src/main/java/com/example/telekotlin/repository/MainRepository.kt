package com.example.telekotlin.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.telekotlin.R
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.repository.roomDb.NoteDao
import com.example.telekotlin.util.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val application: Application
) {


    private lateinit var allNoteLiveData: LiveData<List<Note>>

//    private val _allTele = MutableLiveData<List<Note>>()
//
//    private val allTele: LiveData<List<Note>>
//        get() = _allTele


    init {
       // getAllTeles()
    }



     fun setDate(date: String){
         allNoteLiveData =   noteDao.getNotesWithDate(date)
    }

    private fun getAllTeles() {
        allNoteLiveData = noteDao.getAllTele()
    }


    fun deleteAlltele() {
        CoroutineScope(IO).launch {
            noteDao.deleteAllTele()
        }
    }

    fun deleteTele(id: Int) {


        CoroutineScope(IO).launch {
            noteDao.deleteTele(id)
        }
    }


    fun insertDummyData() {

        val tele = Note(
            application.getString(R.string.lores_ipsum),
            application.getString(R.string.some_text),
            null,
            AppUtils.getDate(),
            null,
            "",
            false
        )

        CoroutineScope(IO).launch {
            noteDao.insert(tele)
        }

    }


    fun completeTask(note: Note){
         CoroutineScope(IO).launch {
             noteDao.updateCompleteTask(note.id,true)
         }
    }


    fun getAsLiveData(): LiveData<List<Note>> {


        return allNoteLiveData
    }


}