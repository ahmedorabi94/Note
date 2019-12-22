package com.example.telekotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telekotlin.viewModels.DetailViewModel
import com.example.telekotlin.viewModels.ListItemViewModel
import com.example.telekotlin.viewModels.SignatureViewModel
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
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignatureViewModel::class)
    abstract fun bindSignatureViewModel(signatureViewModel: SignatureViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: TeleViewModelFactory): ViewModelProvider.Factory
}