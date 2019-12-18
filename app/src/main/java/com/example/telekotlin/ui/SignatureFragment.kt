package com.example.telekotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.telekotlin.databinding.FragmentSignatureBinding
import com.github.gcacace.signaturepad.views.SignaturePad


class SignatureFragment : Fragment(), SignaturePad.OnSignedListener {


    private lateinit var binding: FragmentSignatureBinding

    private lateinit var signaturePad: SignaturePad

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSignatureBinding.inflate(inflater, container, false)

        signaturePad = binding.signatureView

        signaturePad.setOnSignedListener(this)



        binding.saveSignature.setOnClickListener {

            val bitmap = signaturePad.transparentSignatureBitmap


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

}
