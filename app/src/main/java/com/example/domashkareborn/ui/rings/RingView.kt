package com.example.domashkareborn.ui.rings

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.AbsSavedState
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.domashkareborn.R
import com.example.domashkareborn.ui.homework.TaskView
import kotlinx.parcelize.Parcelize

class RingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val currentSubject: TextView
    val firstTime: TextView
    val secondTime: TextView

    init{
        LayoutInflater.from(context).inflate(R.layout.view_clock_subject, this, true)

        currentSubject = findViewById(R.id.tvSubjectNum)
        firstTime = findViewById(R.id.clockStart)
        secondTime = findViewById(R.id.clockEnd)

    }

    public fun setFirstTime(time:String){
        firstTime.text = time
    }
    public fun setSecondTime(time:String){
        secondTime.text = time
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()!!
        val state = SavedState(superState)
        state.time = SavedState.Time(firstTime.text.toString(), secondTime.text.toString(), currentSubject.text.toString())

        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        state.time?.also {
            firstTime.text = it.firstTime
            secondTime.text = it.secondTime
            currentSubject.text = it.subjectNum
        }
        invalidate()
    }


    class SavedState : AbsSavedState{

        var time:Time? = null

        constructor(superState: Parcelable) : super(superState)

        @Suppress("unused", "UNCHECKED_CAST")
        constructor(source: Parcel, loader: ClassLoader) : super(source, loader) {
            time = source.readParcelable(loader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(time, flags)
        }

        @Parcelize
        data class Time(
            val firstTime:String,
            val secondTime:String,
            var subjectNum:String
        ):Parcelable
    }
}