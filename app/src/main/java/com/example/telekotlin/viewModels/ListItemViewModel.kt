package com.example.telekotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.telekotlin.repository.MainRepository
import com.example.telekotlin.repository.data.Note
import javax.inject.Inject


class ListItemViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {


    private var allNoteLiveData: LiveData<List<Note>> = repo.getAsLiveData()


    fun deleteAllTele() {
        repo.deleteAlltele()
    }

    fun insertDummyTele() {
        repo.insertDummyData()
    }

    fun deleteTele(id: Int) {
        repo.deleteTele(id)
    }


    fun getAllTeleLiveData(): LiveData<List<Note>> {
        return allNoteLiveData
    }


}