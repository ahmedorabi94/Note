package com.example.telekotlin.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.telekotlin.R
import com.example.telekotlin.databinding.FragmentListItemBinding
import com.example.telekotlin.di.Injectable
import com.example.telekotlin.repository.data.Note
import com.example.telekotlin.viewModels.ListItemViewModel
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.google.android.material.internal.NavigationMenu
import io.github.yavski.fabspeeddial.FabSpeedDial
import org.joda.time.DateTime
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject


private const val REQUEST_CODE = 100

class ListItemFragment : Fragment(), Injectable, NoteCallback, PopupMenu.OnMenuItemClickListener,
    DatePickerListener {
    override fun completeTask(note: Note, isChecked: Boolean, position: Int) {

//        Log.e("ListFragment", isChecked.toString())
//
//        if (isChecked) {
//           // viewModel.completeTask(note, dateStr)
//        }
//
//
//        noteAdapter.notifyItemChanged(position)
//

    }


    private val thread = Thread(Runnable {
        Thread.sleep(900)

        activity!!.runOnUiThread {
            noteAdapter.notifyItemChanged(position)
        }
    })


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListItemViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: FragmentListItemBinding

    private lateinit var noteAdapter: NoteAdapter

    private lateinit var datePicker: HorizontalPicker

    private var dateStr: String = ""

    private var position: Int = -1

    private val clearPaint =
        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentListItemBinding.inflate(inflater, container, false)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListItemViewModel::class.java)

        binding.viewmodel = viewModel
        binding.invalidateAll()
        //    binding.executePendingBindings()

        activity!!.title = "Notes"
        setHasOptionsMenu(true)


        viewModel.position.observe(this.viewLifecycleOwner, Observer {


            Thread(Runnable {
                Thread.sleep(100)

                activity!!.runOnUiThread {
                    noteAdapter.notifyItemChanged(it)
                }
            }).start()


            //  position = it

            //  thread.start()

            //            recyclerView.addOnLayoutChangeListener { p0, p1, p2, p3, p4, p5, p6, p7, p8 ->
//                noteAdapter.notifyItemChanged(it )
//            }

            // activity!!.runOnUiThread { recyclerView.adapter!!.notifyItemChanged(it) }

//            recyclerView.post {
//                noteAdapter.notifyItemChanged(it)
//            }
        })




        binding.fabBtn.setMenuListener(object : FabSpeedDial.MenuListener {
            override fun onMenuClosed() {
            }

            override fun onPrepareMenu(p0: NavigationMenu?): Boolean {
                return true
            }

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when (menuItem!!.itemId) {
                    R.id.text_note_item -> {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_listItemFragment_to_noteDetailsFragment)
                        return true
                    }
                    R.id.signature_item -> {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_listItemFragment_to_signatureFragment)
                        return true
                    }
                    R.id.attach_item -> {
                        Log.e("ListFragment", "Attach Item")
                        return true
                    }
                    R.id.audio_item -> {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_listItemFragment_to_audioRecordFragment)
                        return true
                    }
                    R.id.reminder_item -> {
                        val fragmentManager = fragmentManager
                        val fragment = ReminderDialogFragment()
                        if (fragmentManager != null) {
                            fragment.show(fragmentManager, "fragment_reminder")
                        }

                        return true
                    }
                }

                return false
            }

        })


        val deleteIcon =
            ContextCompat.getDrawable(context!!, R.drawable.ic_delete_grey600_24dp)
        val intrinsicWidth = deleteIcon!!.intrinsicWidth
        val intrinsicHeight = deleteIcon.intrinsicHeight
        val background = ColorDrawable()
        val backgroundColor = Color.parseColor("#f44336")


//        viewModel.getAllTeleLiveData().observe(this, Observer {
//            Log.e("ListItem", "${it.size}")
//
//            if (it.isEmpty()) {
//                binding.emptyTextIcon.visibility = View.VISIBLE
//                binding.emptyTitleText.visibility = View.VISIBLE
//
//            } else {
//                binding.emptyTextIcon.visibility = View.GONE
//                binding.emptyTitleText.visibility = View.GONE
//            }
//
//            noteAdapter.submitList(it)
//        })


//        val itemTouchHelperCallback =
//            object : ItemTouchHelper.SimpleCallback(
//                0, ItemTouchHelper.LEFT
//                        or ItemTouchHelper.RIGHT
//            ) {
//                override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    target: RecyclerView.ViewHolder
//                ): Boolean {
//                    return false
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    viewModel.deleteTele(noteAdapter.getNote(viewHolder.adapterPosition).id)
//                }
//
//                override fun onChildDraw(
//                    c: Canvas,
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    dX: Float,
//                    dY: Float,
//                    actionState: Int,
//                    isCurrentlyActive: Boolean
//                ) {
//
//                    val itemView = viewHolder.itemView
//                    val itemHeight = itemView.bottom - itemView.top
//                    val isCanceled = dX == 0f && !isCurrentlyActive
//
//                    if (isCanceled) {
//                        clearCanvas(
//                            c,
//                            itemView.right + dX,
//                            itemView.top.toFloat(),
//                            itemView.right.toFloat(),
//                            itemView.bottom.toFloat()
//                        )
//                        super.onChildDraw(
//                            c,
//                            recyclerView,
//                            viewHolder,
//                            dX,
//                            dY,
//                            actionState,
//                            isCurrentlyActive
//                        )
//                        return
//                    }
//
//                    // Draw the red delete background
//                    background.color = backgroundColor
//                    background.setBounds(
//                        itemView.right + dX.toInt(),
//                        itemView.top,
//                        itemView.right,
//                        itemView.bottom
//                    )
//                    background.draw(c)
//
//                    // Calculate position of delete icon
//                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
//                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
//                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
//                    val deleteIconRight = itemView.right - deleteIconMargin
//                    val deleteIconBottom = deleteIconTop + intrinsicHeight
//
//                    // Draw the delete icon
//                    deleteIcon.setBounds(
//                        deleteIconLeft,
//                        deleteIconTop,
//                        deleteIconRight,
//                        deleteIconBottom
//                    )
//                    deleteIcon.draw(c)
//
//                    super.onChildDraw(
//                        c,
//                        recyclerView,
//                        viewHolder,
//                        dX,
//                        dY,
//                        actionState,
//                        isCurrentlyActive
//                    )
//
//
//                }
//            }
//
//        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//
//


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setAdapter()
        setupDatePicker()

    }


    private fun setupDatePicker() {
        datePicker = binding.datePicker

        datePicker
            .setListener(this)
            .init()

        datePicker.setDate(DateTime())
    }


    private fun setAdapter() {

        val viewmodel = binding.viewmodel

        if (viewmodel != null) {
            recyclerView = binding.recyclerView
            noteAdapter = NoteAdapter(viewmodel, this, this.viewLifecycleOwner)
            recyclerView.adapter = noteAdapter

        }

    }


    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }


    override fun onDateSelected(dateSelected: DateTime?) {
        Log.e("ListFragment", dateSelected.toString().substring(0, 10))
        dateStr = dateSelected.toString().substring(0, 10)
        viewModel.setData(dateStr)
    }


//    private fun initRecyclerView() {
//        recyclerView = binding.recyclerView
//        recyclerView.setHasFixedSize(true)
//        // recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//
//        val linearLayout = LinearLayoutManager(activity)
//        recyclerView.layoutManager = linearLayout
//    }
//

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
        arg.putString("fileName", note.audioName)

        if (note.signature != null) {
            arg.putBoolean("isSign", true)
        }

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_listItemFragment_to_noteDetailsFragment, arg)


    }

    override fun onAudioClick(note: Note) {
        //  startPlaying(note.audioName!!)
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
                    fragment.show(fragmentManager, "fragment_reminder")
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

        Log.e("ListFragment", stringBuilder.toString())

        inputStream.close()
        reader.close()
        return stringBuilder.toString()
    }



}
