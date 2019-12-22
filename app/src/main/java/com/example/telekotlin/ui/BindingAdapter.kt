package com.example.telekotlin.ui

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("setImage")
fun setImage(imageView: ImageView, byteArray: ByteArray?) {

    if (byteArray != null) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        imageView.setImageBitmap(bitmap)
    } else {
        imageView.visibility = View.GONE
    }


}