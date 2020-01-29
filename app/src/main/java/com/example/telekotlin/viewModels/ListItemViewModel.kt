package com.example.telekotlin.viewModels

import androidx.lifecycle.*
import com.example.telekotlin.repository.MainRepository
import com.example.telekotlin.repository.data.Note
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListItemViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {

    private val dateStr = MutableLiveData<String>()

    private val _forceUpdate = MutableLiveData<Boolean>()

    private val _position = MutableLiveData<Int>()

    val position: LiveData<Int> = _position

    private val _openDetailEvent = MutableLiveData<Note>()
    val openDetailEvent: LiveData<Note> = _openDetailEvent


    private val _tasks: LiveData<List<Note>> = _forceUpdate.switchMap { forceUpdate ->

        if (forceUpdate) {
            viewModelScope.launch {
                repo.setDate(dateStr.value!!)
            }
        }
        repo.getAsLiveData()

    }

    val tasks: LiveData<List<Note>> = _tasks

    val empty: LiveData<Boolean> = Transformations.map(_tasks) {
        it.isEmpty()

    }


    fun setData(date: String) {

        dateStr.value = date
        _forceUpdate.value = true

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


    fun openDetailFragment(note: Note) {
        _openDetailEvent.value = note
        _openDetailEvent.value = null
    }


    fun completeTask(note: Note, completed: Boolean, position: Int) {
        viewModelScope.launch {
            if (completed) {
                repo.completeTask(note)
            } else {
                repo.unCompleteTask(note)
            }
        }

        _position.value = position

    }


}