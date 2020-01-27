package com.example.telekotlin.repository.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.telekotlin.repository.data.Note

@Dao
interface NoteDao {


    @Insert
    suspend fun insert(note: Note)

    @Query("select * from tele")
    fun getAllTele(): LiveData<List<Note>>

    @Query("delete from tele")
    suspend fun deleteAllTele()


    @Query("delete from tele where id = :id")
    suspend fun deleteTele(id: Int)

    @Query("UPDATE tele SET title = :title , body = :body WHERE id = :id")
    fun updateTele(id: Int, title: String, body: String)

    @Query("select * from tele where id =:id")
    fun getTele(id: Int): LiveData<Note>


    @Query("select * from tele where dateStr =:date")
     fun getNotesWithDate(date: String): LiveData<List<Note>>


    @Query("update tele set checked =:completed where id =:id")
    suspend fun updateCompleteTask(id: Int, completed: Boolean)


}