package com.example.telekotlin.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.telekotlin.databinding.FragmentSignatureBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.viewModels.SignatureViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class SignatureFragment : Fragment(), SignaturePad.OnSignedListener, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SignatureViewModel

    private lateinit var binding: FragmentSignatureBinding

    private lateinit var signaturePad: SignaturePad

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSignatureBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SignatureViewModel::class.java)


        signaturePad = binding.signatureView

        signaturePad.setOnSignedListener(this)



        binding.saveSignature.setOnClickListener {

            val bitmap = signaturePad.transparentSignatureBitmap


            val byte = convertToByte(bitmap)

            viewModel.insertNote("", "", byte)

            binding.userSignature.setImageBitmap(bitmap)

            Log.e("SignatureFragment", bitmap.toString())

        }

        binding.deleteSignature.setOnClickListener {
            signaturePad.clear()
        }



        return binding.root
    }

    override fun onStartSigning() {
        Log.e("SignatureFragment", "onStartSigning")
    }

    override fun onClear() {
        Log.e("SignatureFragment", "onClear")
    }

    override fun onSigned() {
        Log.e("SignatureFragment", "onSigned")
    }


    private fun convertToByte(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byte = stream.toByteArray()
     //   bitmap.recycle()
        return byte
    }


}
