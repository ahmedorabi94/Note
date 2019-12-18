package com.example.telekotlin.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentListItemBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.viewModels.ListItemViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import javax.inject.Inject


class ListItemFragment : Fragment(), Injectable, NoteCallback, PopupMenu.OnMenuItemClickListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListItemViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: FragmentListItemBinding

    private lateinit var noteAdapter: NoteAdapter

    private val REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentListItemBinding.inflate(inflater, container, false)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListItemViewModel::class.java)

        binding.lifecycleOwner = this



        activity!!.title = "Notes"

        setHasOptionsMenu(true)

        initRecyclerView()

        noteAdapter = NoteAdapter(this)
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

        binding.fab.setOnClickListener {
            showPopup(it)
        }


        return binding.root
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        val linearLayout  = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayout
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


    override fun onNoteClick(note: Note) {
        Log.e("ListFragment", "${note.title} ${note.id}")

        val arg = Bundle()
        arg.putInt("row_id", note.id)

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_listItemFragment_to_noteDetailsFragment, arg)


    }

    private fun showPopup(view: View) {

        val popup = PopupMenu(context, view)
        val inflater = popup.menuInflater
        popup.setOnMenuItemClickListener(this)
        inflater.inflate(R.menu.popup_menu, popup.menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.add_text_popup -> {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_listItemFragment_to_noteDetailsFragment)
                return true
            }

            R.id.import_text -> {
                openFileManager()
                return true
            }

            R.id.signatureItem -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_listItemFragment_to_signatureFragment)
                return true
            }

        }

        return false
    }


    private fun openFileManager() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            val uri = data!!.data

            val text = uri?.let {
                readTextFromUri(it)
            }

            val args = Bundle()
            args.putString("body", text)

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_listItemFragment_to_noteDetailsFragment, args)



        }

    }

    private fun readTextFromUri(uri : Uri) : String{

        val inputStream = context!!.contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream!!))
        val stringBuilder = StringBuilder()
       // var line : String

//        while ((line = reader.readLine()) != null) {
//            stringBuilder.append(line)
//
//        }

        reader.lineSequence().forEach {
            stringBuilder.append(it)

        }

        inputStream.close()
        reader.close()
        return stringBuilder.toString()
    }

}
