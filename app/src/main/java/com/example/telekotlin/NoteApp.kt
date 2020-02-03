package com.example.telekotlin

import android.app.Activity
import android.app.Application
import androidx.work.Constraints
import com.example.telekotlin.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class NoteApp : Application(), HasActivityInjector {



    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)




        val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            .build()


    }

    override fun activityInjector() = dispatchingAndroidInjector

}