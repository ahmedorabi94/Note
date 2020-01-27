package com.example.telekotlin.viewModels

import android.util.Log
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


    //  private var allNoteLiveData: LiveData<List<Note>> = repo.getAsLiveData()


    private val _tasks: LiveData<List<Note>> = _forceUpdate.switchMap { forceUpdate ->

        // Log.e("ViewModelData", dateStr.value!!)

        if (forceUpdate) {
            viewModelScope.launch {
                Log.e("viewModelData",dateStr.value!!)
                repo.setDate(dateStr.value!!)

            }
            //  Log.e("ViewModelData", dateStr.toString())
        }

        repo.getAsLiveData()


//        repo.getAsLiveData().switchMap {
//            setTASSS(it)
//
//        }
    }

    val tasks: LiveData<List<Note>> = _tasks


    private fun setTASSS(tasksResult: List<Note>): LiveData<List<Note>> {

        val result = MutableLiveData<List<Note>>()

        viewModelScope.launch {
            result.value = filterItems(tasksResult)
        }

        return result
    }

    private fun filterItems(tasks: List<Note>): List<Note> {
        val tasksToShow = ArrayList<Note>()
        // We filter the tasks based on the requestType
        for (task in tasks)
            tasksToShow.add(task)


        return tasksToShow
    }

//    private var allNoteLiveData: LiveData<List<Note>> = Transformations.switchMap(dateStr) {
//        repo.getAsLiveData()
//    }


    fun setData(date: String) {


        //  Log.e("ViewModelData", dateStr.value!!)

//        repo.setDate(date)
//        dateStr.value = date


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


//    fun completeTask(note: Note, date: String) {
//        CoroutineScope(IO).launch {
//            repo.completeTask(note)
//
//        }
//
//        repo.setDate(date)
//        dateStr.value = date
//        //  dateStr.value = date
//    }


    fun completeTask(note: Note, completed: Boolean , position : Int) {
        viewModelScope.launch {
            if (completed) {
                repo.completeTask(note)
            }
        }

      //  _forceUpdate.value = true

        _position.value = position

    }


//    fun getTasks(): LiveData<List<Note>> {
//        return _tasks
//    }


}