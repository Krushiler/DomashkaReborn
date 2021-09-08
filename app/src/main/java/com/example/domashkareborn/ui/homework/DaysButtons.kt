package com.example.domashkareborn.ui.homework

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.customview.view.AbsSavedState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domashkareborn.R
import com.example.domashkareborn.databinding.ViewDaysButtonsBinding
import kotlinx.parcelize.Parcelize


class DaysButtons @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val radioButtons:MutableList<RadioButton> = mutableListOf()
    var radioDayGroup:RadioGroup

    init {

        LayoutInflater.from(context).inflate(R.layout.view_days_buttons, this, true)

        radioDayGroup = findViewById(R.id.radioDayGroup)
        radioButtons.add(findViewById(R.id.radioDay1))
        radioButtons.add(findViewById(R.id.radioDay2))
        radioButtons.add(findViewById(R.id.radioDay3))
        radioButtons.add(findViewById(R.id.radioDay4))
        radioButtons.add(findViewById(R.id.radioDay5))
        radioButtons.add(findViewById(R.id.radioDay6))

    }

    public fun getSelectedDay():Int{
        for (i in 0..5){
            if (radioButtons[i].isChecked){
                return i
            }
        }
        return 0
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val state = SavedState(superState)
        state.pickedDay = SavedState.PickedDay(
            getSelectedDay()
        )
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        state.pickedDay?.also {
            radioDayGroup.check(radioButtons[it.pickedDay].id)
        }
        invalidate()
    }

    class SavedState : AbsSavedState {
        var pickedDay: PickedDay? = null

        constructor(superState: Parcelable) : super(superState)

        @Suppress("unused", "UNCHECKED_CAST")
        constructor(source: Parcel, loader: ClassLoader) : super(source, loader) {
            pickedDay = source.readParcelable(loader)
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(pickedDay, flags)
        }

        @Parcelize
        data class PickedDay(
            val pickedDay: Int
        ) : Parcelable
    }

}