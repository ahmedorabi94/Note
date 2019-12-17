package com.example.telekotlin.repository.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.telekotlin.repository.data.Tele

@Dao
interface TeleDao {


    @Insert
    suspend fun insert(tele: Tele)

    @Query("select * from tele")
    fun getAllTele(): LiveData<List<Tele>>

    @Query("delete from tele")
    suspend fun deleteAllTele()


    @Query("delete from tele where id = :id")
    suspend fun deleteTele(id: Int)

    @Query("UPDATE tele SET title = :title , body = :body WHERE id = :id")
    fun updateTele(id: Int, title: String, body: String)

    @Query("select * from tele where id =:id")
    fun getTele(id: Int): LiveData<Tele>


}