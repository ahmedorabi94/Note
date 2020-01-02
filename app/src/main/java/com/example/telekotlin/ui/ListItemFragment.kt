package com.example.telekotlin.ui

import android.app.ActionBar
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject


class ListItemFragment : Fragment(), Injectable, NoteCallback, PopupMenu.OnMenuItemClickListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListItemViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: FragmentListItemBinding

    private lateinit var noteAdapter: NoteAdapter

    private val REQUEST_CODE = 100

    private val p = Paint()

    val clearPaint =
        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentListItemBinding.inflate(inflater, container, false)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListItemViewModel::class.java)

        binding.lifecycleOwner = this


         val deleteIcon =
            ContextCompat.getDrawable(context!!, R.drawable.ic_delete_grey600_24dp)
         val intrinsicWidth = deleteIcon!!.intrinsicWidth
         val intrinsicHeight = deleteIcon.intrinsicHeight
         val background = ColorDrawable()
         val backgroundColor = Color.parseColor("#f44336")



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

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.bottom - itemView.top
                    val isCanceled = dX == 0f && !isCurrentlyActive

                    if (isCanceled) {
                        clearCanvas(
                            c,
                            itemView.right + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                        return
                    }

                    // Draw the red delete background
                    background.color = backgroundColor
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)

                    // Calculate position of delete icon
                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    // Draw the delete icon
                    deleteIcon!!.setBounds(
                        deleteIconLeft,
                        deleteIconTop,
                        deleteIconRight,
                        deleteIconBottom
                    )
                    deleteIcon.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )


                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

//        binding.fab.setOnClickListener {
//            showPopup(it)
//           // setupPopupWindows(it)
//        }


        return binding.root
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        val linearLayout = LinearLayoutManager(activity)
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

        if (note.signature != null){
           arg.putBoolean("isSign",true)
        }

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
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_listItemFragment_to_signatureFragment)
                return true
            }

            R.id.reminderItem -> {

                val fragmentManager = fragmentManager
                val fragment = ReminderDialogFragment()
                if (fragmentManager != null) {
                    fragment.show(fragmentManager,"fragment_reminder")
                }

                //Navigation.findNavController(binding.root).navigate(R.id.action_listItemFragment_to_reminderDialogFragment)
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

    private fun readTextFromUri(uri: Uri): String {

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

        Log.e("ListFragment",stringBuilder.toString())

        inputStream.close()
        reader.close()
        return stringBuilder.toString()
    }


    private fun setupPopupWindows(view: View){

        val inflate : LayoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView  = inflate.inflate(R.layout.pop_layout,null)

        val popupWindows = PopupWindow(popView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true)

        popupWindows.setBackgroundDrawable(BitmapDrawable())
        popupWindows.isOutsideTouchable = true
        popupWindows.setOnDismissListener {

        }

        popView.findViewById<FloatingActionButton>(R.id.addTextFb).setOnClickListener {

            Toast.makeText(activity,"Hello",Toast.LENGTH_SHORT).show()

            popupWindows.dismiss()
        }

        popupWindows.showAsDropDown(view,-100,50)

    }

}
