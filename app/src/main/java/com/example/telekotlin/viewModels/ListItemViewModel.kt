package com.example.telekotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.telekotlin.repository.MainRepository
import com.example.telekotlin.repository.data.Note
import javax.inject.Inject


class ListItemViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {

    private val dateStr = MutableLiveData<String>()


    //  private var allNoteLiveData: LiveData<List<Note>> = repo.getAsLiveData()


    private var allNoteLiveData: LiveData<List<Note>> = Transformations.switchMap(dateStr) {
        repo.getAsLiveData()
    }




    fun setData(date: String) {
        repo.setDate(date)
        dateStr.value = date
    }


    fun deleteAllTele() {
        repo.deleteAlltele()
    }

    fun insertDummyTele() {
        repo.insertDummyData()
    }

    fun deleteTele(id: Int) {
        repo.deleteTele(id)
    }


    fun completeTask(note: Note , date: String) {
        repo.completeTask(note)
        setData(date)
      //  dateStr.value = date
    }


    fun getAllTeleLiveData(): LiveData<List<Note>> {
        return allNoteLiveData
    }


}