package com.example.telekotlin.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.telekotlin.R
import com.example.telekotlin.repository.data.Tele
import com.example.telekotlin.repository.roomDb.TeleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val teleDao: TeleDao,
    private val application: Application
) {


    private lateinit var allTeleLiveData: LiveData<List<Tele>>

//    private val _allTele = MutableLiveData<List<Tele>>()
//
//    private val allTele: LiveData<List<Tele>>
//        get() = _allTele


    init {
        getAllTeles()
    }

    private fun getAllTeles() {
        allTeleLiveData = teleDao.getAllTele()
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

        val tele = Tele(
            application.getString(R.string.lores_ipsum),
            application.getString(R.string.some_text)
        )

        CoroutineScope(IO).launch {
            teleDao.insert(tele)
        }

    }


    fun getAsLiveData(): LiveData<List<Tele>> {
        return allTeleLiveData
    }

}