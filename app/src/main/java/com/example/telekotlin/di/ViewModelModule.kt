package com.example.telekotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telekotlin.viewModels.MainViewModel
import com.example.telekotlin.viewModels.TeleViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMoviesViewModel(mainViewModel: MainViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: TeleViewModelFactory): ViewModelProvider.Factory
}