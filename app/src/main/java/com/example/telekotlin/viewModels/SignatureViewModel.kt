package com.example.telekotlin.viewModels

import androidx.lifecycle.ViewModel
import com.example.telekotlin.repository.SignatureRepository
import javax.inject.Inject


class SignatureViewModel @Inject constructor(private val repo: SignatureRepository) : ViewModel(){


    fun insertNote(title: String, body: String, signImage: ByteArray?) {
        repo.insertNote(title, body, signImage)
    }

}