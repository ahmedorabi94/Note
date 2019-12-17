package com.example.telekotlin.repository.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.repository.roomDb.NoteDao


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

}