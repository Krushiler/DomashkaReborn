package com.example.domashkareborn.ui.homework

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domashkareborn.R
import com.example.domashkareborn.databinding.HomeworkFragmentBinding
import com.example.domashkareborn.di.dataSourceModule
import com.example.domashkareborn.screen.LoginActivity
import com.google.android.play.core.splitinstall.d
import org.koin.android.viewmodel.ext.android.viewModel


class HomeworkFragment : Fragment() {

    companion object {
        fun newInstance() = HomeworkFragment()
    }

    private val viewModel: HomeworkViewModel by viewModel()

    private val binding: HomeworkFragmentBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.homework_fragment, container, false)
    }

    var tasks = mutableListOf<TaskView>()
    var homeworkETs = mutableListOf<EditText>()
    var subjectSPs = mutableListOf<Spinner>()
    var homeworkETIds = mutableListOf<Int>()
    var subjectSPIds = mutableListOf<Int>()
    var taskIds = mutableListOf<Int>()

    val tasksCount = 10
    lateinit var taskLayout: LinearLayout

    var taskDescriptions = MutableList(60){""}
    var taskSubjects = MutableList(60){0}

    lateinit var daysButtonsLayout: DaysButtons
    var currentPickedDay: Int = 0

    var isModerator:Boolean = false

    lateinit var toolbar:Toolbar

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasks = mutableListOf()
        toolbar = view.findViewById(R.id.toolbar)
        taskLayout = view.findViewById(R.id.tasksLayout)
        daysButtonsLayout = view.findViewById(R.id.daysButtons)

        currentPickedDay = daysButtonsLayout.getSelectedDay()

        taskIds = viewModel.getTasksIds(tasksCount).toMutableList()
        homeworkETIds = viewModel.getHomeworkETIds(tasksCount).toMutableList()
        subjectSPIds = viewModel.getSubjectSPIds(tasksCount).toMutableList()

        for (i in 0 until tasksCount) {
            var taskView = TaskView(requireActivity().applicationContext)
            taskView.id = taskIds[i]
            taskView.spinner.id = subjectSPIds[i]
            taskView.editText.id = homeworkETIds[i]
            tasks.add(taskView)
            taskLayout.addView(taskView)
        }

        taskDescriptions = viewModel.homework
        taskSubjects = viewModel.schedule

        viewModel.homeworkLiveData.observe(viewLifecycleOwner) {
            taskDescriptions = it.toMutableList()
            setTaskDescriptionsETs()
        }
        viewModel.scheduleLiveData.observe(viewLifecycleOwner) {
            taskSubjects = it.toMutableList()
            setTaskSubjectsSPs()
        }
        viewModel.additionalSubjectsLiveData.observe(viewLifecycleOwner) {
            for (tv in tasks) {
                tv.additionalSubjects = it
                tv.setSpinnerItems()
            }
        }

        if (daysButtonsLayout.getSelectedDay() == 0){
            daysButtonsLayout.radioButtons[0].isChecked = true
        }

        daysButtonsLayout.radioDayGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            updateTasks()
            currentPickedDay = daysButtonsLayout.getSelectedDay()
            setTaskDescriptionsETs()
            setTaskSubjectsSPs()
        })

        viewModel.isModeratorLiveData.observe(viewLifecycleOwner){
            isModerator = it.getIsModerator()
            toolbar.menu.clear()
            if (!isModerator){
                toolbar.inflateMenu(R.menu.homework_toolbar_nonadmin_menu)
                updateToolbarColors()
                for (tv in tasks){
                    tv.editText.isEnabled = false
                    tv.spinner.isEnabled = false
                }
            }else{
                toolbar.inflateMenu(R.menu.homework_toolbar_menu)
                updateToolbarColors()
                for (tv in tasks){
                    tv.editText.isEnabled = true
                    tv.spinner.isEnabled = true
                }
            }
        }


        toolbar.menu.clear()
        if (isModerator) {
            toolbar.inflateMenu(R.menu.homework_toolbar_menu)
            updateToolbarColors()
        }else{
            toolbar.inflateMenu(R.menu.homework_toolbar_nonadmin_menu)
            updateToolbarColors()
        }
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_savehomework){
                var adb = AlertDialog.Builder(activity)
                var dialogView = layoutInflater.inflate(R.layout.popup_save_homework_dialog, null)
                if (!isModerator){

                }
                var dialog = adb.setView(dialogView).create()
                /*val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window?.attributes)
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT*/
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogView.findViewById<Button>(R.id.cancel).setOnClickListener {
                    dialog.cancel()
                }
                dialogView.findViewById<Button>(R.id.accept).setOnClickListener {
                    updateTasks()
                    viewModel.saveShedule(taskSubjects)
                    viewModel.saveHomework(taskDescriptions)
                    dialog.cancel()
                }
            }
            if (it.itemId == R.id.action_account){
                var adb = AlertDialog.Builder(activity)
                var dialogView = layoutInflater.inflate(R.layout.popup_window_account, null)
                if (!isModerator){
                    dialogView.findViewById<LinearLayout>(R.id.moderatorPopupLayout).visibility = View.GONE
                }
                var dialog = adb.setView(dialogView).create()
                /*val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window?.attributes)
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT*/
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogView.findViewById<ImageButton>(R.id.cancelDialog).setOnClickListener {
                    dialog.cancel()
                }
                dialogView.findViewById<Button>(R.id.logoutBTN).setOnClickListener {
                    viewModel.logout()
                    dialog.cancel()
                }
                dialogView.findViewById<Button>(R.id.changeSubjectsBTN).setOnClickListener {
                    viewModel.goToSubjects()
                    dialog.cancel()
                }
                /*dialog.window?.attributes=lp*/
            }
            true
        }

        viewModel.logout.observe(viewLifecycleOwner){
            if (it){
                var intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

    }

    private fun updateTasks(){
        for (i in 0 until tasksCount){
            if (tasks[i].getHomework() != "") {
                taskDescriptions[i + 10 * currentPickedDay] = tasks[i].getHomework()
            }
            if (tasks[i].getSubjectPos() != 0) {
                taskSubjects[i + 10 * currentPickedDay] = tasks[i].getSubjectPos()
            }
        }
    }

    override fun onDestroyView() {
        updateTasks()
        var isloaded = false
        for (i in taskDescriptions){
            if (i != ""){
                isloaded = true
            }
        }
        if (isloaded) {
            viewModel.homework = taskDescriptions
            viewModel.schedule = taskSubjects
            viewModel.setLiveData()
        }
        super.onDestroyView()
    }

    private fun setTaskDescriptionsETs() {
        for (i in 0 until tasksCount) {
            tasks[i].setHomework(taskDescriptions[i + 10 * currentPickedDay])
        }
    }

    fun updateToolbarColors(){
        for (i in 0 until toolbar.menu.size){
            var icon = toolbar.menu.getItem(i).icon
            icon = DrawableCompat.wrap(icon)
            DrawableCompat.setTint(icon, ContextCompat.getColor(requireActivity(), R.color.white))
            toolbar.menu.getItem(i).icon = icon
        }
    }

    private fun setTaskSubjectsSPs() {
        for (i in 0 until tasksCount) {
            tasks[i].setSubjectPos(taskSubjects[i + 10 * currentPickedDay])
        }
    }

}