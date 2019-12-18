package com.example.telekotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentNoteDetailsBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.viewModels.DetailViewModel
import javax.inject.Inject


class NoteDetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailViewModel

    private lateinit var binding: FragmentNoteDetailsBinding

    private var rowId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg = arguments
        if (arg != null) {
            rowId = arg.getInt("row_id")

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)


        setHasOptionsMenu(true)

        if (rowId != -1) {

            activity!!.title = "Edit Note"

            viewModel.getNote(rowId)

            viewModel.getNoteLiveData().observe(this, Observer {
                Log.e("NoteDetail", it.body)

                binding.edTitle.setText(it.title)
                binding.edBody.setText(it.body)
            })
        }else{
            activity!!.title = "Add Note"

        }

        binding.saveNoteFab.setOnClickListener {
            saveNote(binding.edTitle.text.toString(), binding.edBody.text.toString())
            Navigation.findNavController(binding.root).navigateUp()
        }



        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.add_text_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_current_item -> {
                viewModel.deleteNote(rowId)
                Navigation.findNavController(binding.root).navigateUp()
                return true
            }

            R.id.save_btn -> {
                saveNote(binding.edTitle.text.toString(), binding.edBody.text.toString())
                Navigation.findNavController(binding.root).navigateUp()

                return true
            }
        }



        return super.onOptionsItemSelected(item)

    }


    private fun saveNote(title: String, body: String) {
        if (rowId == -1) {
            viewModel.insertNewNote(title, body)

        } else {
            viewModel.updateNote(rowId, title, body)
        }
    }


}
