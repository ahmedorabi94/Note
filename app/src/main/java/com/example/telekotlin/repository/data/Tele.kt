package com.example.telekotlin.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tele")
data class Tele(
    val title: String,
    val body: String

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}