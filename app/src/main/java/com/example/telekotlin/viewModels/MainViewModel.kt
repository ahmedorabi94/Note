package com.example.telekotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.telekotlin.repository.MainRepository
import com.example.telekotlin.repository.data.Tele
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {


    private var allTeleLiveData: LiveData<List<Tele>> = repo.getAsLiveData()


    fun deleteAllTele() {
        repo.deleteAlltele()
    }

    fun insertDummyTele() {
        repo.insertDummyData()
    }

    fun deleteTele(id: Int) {
        repo.deleteTele(id)
    }


    fun getAllTeleLiveData(): LiveData<List<Tele>> {
        return allTeleLiveData
    }


}