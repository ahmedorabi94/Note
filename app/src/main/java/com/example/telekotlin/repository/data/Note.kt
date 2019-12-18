package com.example.telekotlin.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tele")
data class Note(
    val title: String,
    val body: String,
    val signature: Byte?

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}