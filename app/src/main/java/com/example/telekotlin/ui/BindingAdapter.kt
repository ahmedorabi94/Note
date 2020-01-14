package com.example.telekotlin.ui

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.telekotlin.repository.data.Note


@BindingAdapter("setImage")
fun setImage(imageView: ImageView, note: Note) {

    if (note.signature != null) {
        imageView.visibility = View.VISIBLE
        val bitmap = BitmapFactory.decodeByteArray(note.signature, 0, note.signature.size)
        imageView.setImageBitmap(bitmap)
    } else {
        imageView.visibility = View.GONE
    }
}


@BindingAdapter("setAudioRecord")
fun setAudioRecord(imageView: ImageView, note: Note) {
    if (note.audioName != null) {
        imageView.visibility = View.VISIBLE
    } else {
        imageView.visibility = View.GONE
    }
}


