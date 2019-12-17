package com.example.telekotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentListItemBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.viewModels.ListItemViewModel
import javax.inject.Inject


class ListItemFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListItemViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: FragmentListItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentListItemBinding.inflate(inflater, container, false)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListItemViewModel::class.java)

        binding.lifecycleOwner = this
        initRecyclerView()

        setHasOptionsMenu(true)

        val adapter = NoteAdapter()
        recyclerView.adapter = adapter



        viewModel.getAllTeleLiveData().observe(this, Observer {
            Log.e("ListItem", "${it.size}")

            if (it.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE

            }
            adapter.submitList(it)
        })



        return binding.root
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.insert_dummy_data -> {
                viewModel.insertDummyTele()
                return true
            }

            R.id.delete_all_data -> {
                viewModel.deleteAllTele()
                return true
            }
        }



        return super.onOptionsItemSelected(item)

    }

}
