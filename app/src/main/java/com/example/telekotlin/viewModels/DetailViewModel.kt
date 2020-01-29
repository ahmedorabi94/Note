package com.example.telekotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.telekotlin.repository.DetailRepository
import com.example.telekotlin.repository.data.Note
import javax.inject.Inject


class DetailViewModel @Inject constructor(private val repo: DetailRepository) : ViewModel() {

    private val mId = MutableLiveData<Int>()

    private var noteLiveData: LiveData<Note> = Transformations.switchMap(mId) {
        repo.getAsLiveData()
    }


    fun getNote(id: Int) {
        repo.getNote(id)
        mId.value = id
    }

    fun insertNewNote(title: String, body: String , date : String , time : String) {
        repo.insertNewNote(title, body , date , time)
    }

    fun updateNote(id: Int, title: String, body: String) {
        repo.updateNote(id, title, body)
    }

    fun deleteNote(id: Int) {
        repo.deleteNote(id)
    }

    fun getNoteLiveData(): LiveData<Note> {
        return noteLiveData
    }

}