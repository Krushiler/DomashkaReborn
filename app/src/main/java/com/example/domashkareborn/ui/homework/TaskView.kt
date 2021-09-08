package com.example.domashkareborn.ui.homework

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.customview.view.AbsSavedState
import com.example.domashkareborn.R
import kotlinx.coroutines.selects.select
import kotlinx.parcelize.Parcelize

class TaskView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val spinner: Spinner
    val editText: EditText
    var defaultSubList:MutableList<String> =
        resources.getStringArray(R.array.defaultSubjects).toMutableList()
    var subList = mutableListOf<String>()
    var additionalSubjects: List<String> = listOf()

    init {

        LayoutInflater.from(context).inflate(R.layout.view_task, this, true)

        spinner = findViewById(R.id.subjectSP)
        editText = findViewById(R.id.homeworkET)

        subList.addAll(defaultSubList)
        subList.addAll(additionalSubjects)
        var adapter = ArrayAdapter<String>(context, R.layout.subject_spinner_item, subList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    public fun setSpinnerItems() {
        var curSelection = spinner.selectedItemPosition
        subList.clear()
        subList.addAll(defaultSubList)
        subList.addAll(additionalSubjects)
        var adapter = ArrayAdapter<String>(context, R.layout.subject_spinner_item, subList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        if (curSelection < subList.size) {
            spinner.setSelection(curSelection)
        }
    }

    public fun getHomework(): String {
        return editText.text.toString()
    }

    public fun getSubjectPos(): Int {
        return spinner.selectedItemPosition
    }

    public fun getSubject(): String {
        return spinner.selectedItem.toString()
    }

    public fun setHomework(homework: String) {
        editText.setText(homework)
    }

    public fun setSubjectPos(subjectPos: Int) {
        if (subjectPos >= subList.size){
            spinner.setSelection(0)
        }else{
            spinner.setSelection(subjectPos)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val state = SavedState(superState)
        state.description = SavedState.TaskDescription(
            additionalSubjects,
            spinner.selectedItemPosition,
            editText.text.toString()
        )
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        state.description?.also {
            additionalSubjects = it.subjectList
            setSpinnerItems()
            spinner.setSelection(it.selectedSubject)
            editText.setText(it.taskText)
        }
        invalidate()
    }

    class SavedState : AbsSavedState {
        var description: TaskDescription? = null

        constructor(superState: Parcelable) : super(superState)

        @Suppress("unused", "UNCHECKED_CAST")
        constructor(source: Parcel, loader: ClassLoader) : super(source, loader) {
            description = source.readParcelable(loader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(description, flags)
        }

        @Parcelize
        data class TaskDescription(
            val subjectList: List<String>,
            val selectedSubject: Int,
            val taskText: String
        ) : Parcelable
    }

}