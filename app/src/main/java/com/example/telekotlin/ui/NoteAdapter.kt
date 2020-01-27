package com.example.telekotlin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.telekotlin.databinding.ListItemBinding
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.viewModels.ListItemViewModel

class NoteAdapter(
    private val viewModel: ListItemViewModel,
    private val callback: NoteCallback,
    private val lifecycleOwner: LifecycleOwner
) :
    ListAdapter<Note, NoteAdapter.MyViewHolder>(DiffCallback) {


    companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.callback = callback


        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, lifecycleOwner, position, viewModel)
    }

    class MyViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            note: Note,
            lifecycleOwner: LifecycleOwner,
            position: Int,
            viewModel: ListItemViewModel
        ) {
            binding.viewmodel = viewModel
            binding.note = note
             binding.position = position
            binding.lifecycleOwner = lifecycleOwner
            binding.invalidateAll()
            binding.executePendingBindings()
        }

    }


    fun getNote(position: Int): Note {
        return getItem(position)
    }


}