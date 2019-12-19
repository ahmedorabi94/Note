package com.example.telekotlin.di

import com.example.telekotlin.ui.ListItemFragment
import com.example.telekotlin.ui.NoteDetailsFragment
import com.example.telekotlin.ui.SignatureFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeListItemFragment(): ListItemFragment


    @ContributesAndroidInjector
    abstract fun contributeNoteDetailsFragment(): NoteDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeSignatureFragment(): SignatureFragment
}