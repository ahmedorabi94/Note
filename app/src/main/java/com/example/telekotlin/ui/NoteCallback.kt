package com.example.telekotlin.ui

import com.example.telekotlin.repository.data.Note

interface NoteCallback {

    fun onNoteClick(note: Note)

    fun onAudioClick(note: Note)
}