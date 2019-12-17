package com.example.telekotlin.di

import android.app.Application
import androidx.room.Room
import com.example.telekotlin.repository.roomDb.AppDatabase
import com.example.telekotlin.repository.roomDb.TeleDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {


    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {

        return Room.databaseBuilder(app, AppDatabase::class.java, "tele_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideDao(database: AppDatabase): TeleDao {
        return database.teleDao
    }

}