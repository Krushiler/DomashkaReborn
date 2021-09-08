 package com.example.domashkareborn.ui.subjects

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domashkareborn.R
import com.example.domashkareborn.screen.MainActivity
import com.example.domashkareborn.ui.subjects.itemtouchhelper.LinearItemTouchCallback
import com.example.domashkareborn.ui.subjects.itemtouchhelper.OnStartDragListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.viewmodel.ext.android.viewModel


class SubjectsFragment : Fragment(), OnStartDragListener {

    val viewModel:SubjectsViewModel by viewModel()

    companion object {
        fun newInstance() = SubjectsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subjects_fragment, container, false)
    }

    val subjects: MutableList<String> = mutableListOf()

    lateinit var subjectsRecyclerView:RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var addSubjectButton:FloatingActionButton

    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)

        val subjectsAdapter:SubjectsAdapter = SubjectsAdapter(subjects, this)

        subjectsRecyclerView = view.findViewById(R.id.subjectsRecyclerView)

        subjectsAdapter.itemClickListener = object : SubjectsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var adb = AlertDialog.Builder(activity)
                var dialogView = layoutInflater.inflate(R.layout.popup_add_subject, null)
                var dialog = adb.setView(dialogView).create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogView.findViewById<Button>(R.id.cancel).setOnClickListener {
                    dialog.cancel()
                }
                dialogView.findViewById<EditText>(R.id.subjectName).setText(subjects[position])
                dialogView.findViewById<TextView>(R.id.label).text = "Изменить предмет"
                dialogView.findViewById<Button>(R.id.accept).text = "Изменить"
                dialogView.findViewById<Button>(R.id.accept).setOnClickListener {
                    var enteredText = dialogView.findViewById<EditText>(R.id.subjectName).text.toString()
                    if (enteredText.trim().isNotEmpty()) {
                        subjects[position] = enteredText
                        subjectsAdapter.notifyDataSetChanged()
                    }
                    dialog.cancel()
                }
            }
        }

        subjectsRecyclerView.adapter = subjectsAdapter
        val layoutManager = LinearLayoutManager(activity)
        subjectsRecyclerView.layoutManager = layoutManager

        val callback: ItemTouchHelper.Callback = LinearItemTouchCallback(subjectsAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper!!.attachToRecyclerView(subjectsRecyclerView)

        addSubjectButton = view.findViewById(R.id.addSubjectBTN)

        subjects.clear()
        subjects.addAll(viewModel.subjects)
        subjectsAdapter.notifyDataSetChanged()

        viewModel.subjectsLiveData.observe(viewLifecycleOwner){
            subjects.clear()
            subjects.addAll(it)
            subjectsAdapter.notifyDataSetChanged()
        }

        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.subjects_toolbar_menu)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        updateToolbarColors()

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_savesubjects){
                var adb = AlertDialog.Builder(activity)
                var dialogView = layoutInflater.inflate(R.layout.popup_save_homework_dialog, null)
                dialogView.findViewById<TextView>(R.id.description).text = "Сохранить предметы"
                var dialog = adb.setView(dialogView).create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogView.findViewById<Button>(R.id.cancel).setOnClickListener {
                    dialog.cancel()
                }
                dialogView.findViewById<Button>(R.id.accept).setOnClickListener {
                    viewModel.saveSubjects(subjects)
                    dialog.cancel()
                }
            }
            true
        }

        addSubjectButton.setOnClickListener {

            var adb = AlertDialog.Builder(activity)
            var dialogView = layoutInflater.inflate(R.layout.popup_add_subject, null)
            var dialog = adb.setView(dialogView).create()
            dialog.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogView.findViewById<Button>(R.id.cancel).setOnClickListener {
                dialog.cancel()
            }
            dialogView.findViewById<Button>(R.id.accept).setOnClickListener {
                var enteredText = dialogView.findViewById<EditText>(R.id.subjectName).text.toString()
                if (enteredText.trim().isNotEmpty()) {
                    subjects.add(enteredText)
                    subjectsAdapter.notifyDataSetChanged()
                }
                dialog.cancel()
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper!!.startDrag(viewHolder!!)
    }

    fun updateToolbarColors(){
        for (i in 0 until toolbar.menu.size){
            var icon = toolbar.menu.getItem(i).icon
            icon = DrawableCompat.wrap(icon)
            DrawableCompat.setTint(icon, ContextCompat.getColor(requireActivity(), R.color.white))
            toolbar.menu.getItem(i).icon = icon
        }
    }
}