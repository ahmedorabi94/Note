package com.example.telekotlin.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentSignatureBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.viewModels.SignatureViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.floatingactionbutton.FloatingActionButton
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



        binding.colorTv.setOnClickListener {
            setupPopupWindows(it)
        }



        binding.saveSignature.setOnClickListener {

            val bitmap = signaturePad.transparentSignatureBitmap


            val byte = convertToByte(bitmap)

            viewModel.insertNote("", "", byte)

            Navigation.findNavController(binding.root).navigateUp()


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


    private fun setupPopupWindows(view: View) {

        val inflate: LayoutInflater =
            activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView = inflate.inflate(R.layout.pop_layout, null)

        val popupWindows = PopupWindow(
            popView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindows.setBackgroundDrawable(BitmapDrawable())
        popupWindows.isOutsideTouchable = true
        popupWindows.setOnDismissListener {

        }

        popView.findViewById<FloatingActionButton>(R.id.blackFab).setOnClickListener {

            signaturePad.setPenColor(Color.BLACK)
            signaturePad.setMinWidth(3F)
         //   signaturePad.setMaxWidth(7F)

            popupWindows.dismiss()
        }

        popView.findViewById<FloatingActionButton>(R.id.blueFab).setOnClickListener {

            signaturePad.setPenColor(Color.BLUE)

            popupWindows.dismiss()
        }

        popView.findViewById<FloatingActionButton>(R.id.redFab).setOnClickListener {

            signaturePad.setPenColor(Color.RED)

            popupWindows.dismiss()
        }

        popView.findViewById<FloatingActionButton>(R.id.greenFab).setOnClickListener {

            signaturePad.setPenColor(Color.GREEN)

            popupWindows.dismiss()
        }


        popupWindows.showAsDropDown(view, -100, 50)

    }


}
