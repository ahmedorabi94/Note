package com.example.telekotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telekotlin.viewModels.ListItemViewModel
import com.example.telekotlin.viewModels.TeleViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(ListItemViewModel::class)
    abstract fun bindMoviesViewModel(listItemViewModel: ListItemViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: TeleViewModelFactory): ViewModelProvider.Factory
}