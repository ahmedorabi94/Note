package com.example.telekotlin.ui

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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


@BindingAdapter("setCrossText")
fun setCrossText(textView: TextView, note: Note) {

    if (note.checked) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

    }

}


