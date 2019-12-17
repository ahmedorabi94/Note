package com.example.telekotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
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

    private lateinit var noteAdapter: NoteAdapter

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

        noteAdapter = NoteAdapter()
        recyclerView.adapter = noteAdapter



        viewModel.getAllTeleLiveData().observe(this, Observer {
            Log.e("ListItem", "${it.size}")

            if (it.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE

            }
            noteAdapter.submitList(it)
        })


        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT
                        or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteTele(noteAdapter.getNote(viewHolder.adapterPosition).id)
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


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
