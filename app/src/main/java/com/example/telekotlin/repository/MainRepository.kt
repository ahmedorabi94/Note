package com.example.telekotlin.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.telekotlin.R
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.repository.roomDb.TeleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val teleDao: TeleDao,
    private val application: Application
) {


    private lateinit var allNoteLiveData: LiveData<List<Note>>

//    private val _allTele = MutableLiveData<List<Note>>()
//
//    private val allTele: LiveData<List<Note>>
//        get() = _allTele


    init {
        getAllTeles()
    }

    private fun getAllTeles() {
        allNoteLiveData = teleDao.getAllTele()
    }


    fun deleteAlltele() {
        CoroutineScope(IO).launch {
            teleDao.deleteAllTele()
        }
    }

    fun deleteTele(id: Int) {

        CoroutineScope(IO).launch {
            teleDao.deleteTele(id)
        }
    }


    fun insertDummyData() {

        val tele = Note(
            application.getString(R.string.lores_ipsum),
            application.getString(R.string.some_text)
        )

        CoroutineScope(IO).launch {
            teleDao.insert(tele)
        }

    }


    fun getAsLiveData(): LiveData<List<Note>> {
        return allNoteLiveData
    }

}