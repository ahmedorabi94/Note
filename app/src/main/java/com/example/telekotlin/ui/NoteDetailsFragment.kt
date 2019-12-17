package com.example.telekotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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



        if (rowId != -1) {
            viewModel.getNote(rowId)

            viewModel.getNoteLiveData().observe(this, Observer {
                Log.e("NoteDetail", it.body)

                binding.edTitle.setText(it.title)
                binding.edBody.setText(it.body)
            })
        }



        return binding.root
    }

}
