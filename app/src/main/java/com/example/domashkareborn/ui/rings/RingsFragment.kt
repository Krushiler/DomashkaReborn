package com.example.domashkareborn.ui.rings

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.res.stringArrayResource
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.domashkareborn.R
import com.google.android.play.core.internal.t
import org.koin.android.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
import java.util.*
import kotlin.math.min


class RingsFragment : Fragment() {

    val viewModel:RingsViewModel by viewModel()

    companion object {
        fun newInstance() = RingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rings_fragment, container, false)
    }

    var isModerator:Boolean = false
    var timeStrings = MutableList(20){""}
    var ringViews = mutableListOf<RingView>()
    lateinit var toolbar: Toolbar
    lateinit var ringsLayout:LinearLayout

    var dateAndTime= Calendar.getInstance()


    var ringIds = mutableListOf<Int>()
    var firstIds = mutableListOf<Int>()
    var secondIds = mutableListOf<Int>()
    var count = 10
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ringViews = mutableListOf()
        ringsLayout = view.findViewById(R.id.ringsLayout)
        ringIds = viewModel.getRingIds(count).toMutableList()
        firstIds = viewModel.getFirstIds(count).toMutableList()
        secondIds = viewModel.getSecondIds(count).toMutableList()
        toolbar = view.findViewById(R.id.toolbar)

        for (i in 0 until count){
            var ringView = RingView(requireActivity().applicationContext)
            ringView.id = ringIds[i]
            ringView.firstTime.id = firstIds[i]
            ringView.secondTime.id = secondIds[i]
            ringView.currentSubject.text = "Урок " + (i+1).toString()
            ringViews.add(ringView)
            ringsLayout.addView(ringView)
            ringView.firstTime.setOnClickListener(View.OnClickListener {
                TimePickerDialog(
                    activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        var hourS = hourOfDay.toString()
                        if (hourOfDay < 10){
                            hourS = "0$hourOfDay"
                        }
                        var minuteS = minute.toString()
                        if (minute < 10){
                            minuteS = "0$minute"
                        }
                        ringView.setFirstTime("$hourS:$minuteS")
                    },
                    dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE), true
                )
                    .show()
            })
            ringView.secondTime.setOnClickListener(View.OnClickListener {
                TimePickerDialog(
                    activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        var hourS = hourOfDay.toString()
                        if (hourOfDay < 10){
                            hourS = "0$hourOfDay"
                        }
                        var minuteS = minute.toString()
                        if (minute < 10){
                            minuteS = "0$minute"
                        }
                        ringView.setSecondTime("$hourS:$minuteS")

                    },
                    dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE), true
                )
                    .show()
            })

        }

        timeStrings = viewModel.rings
        setRings()

        viewModel.ringsLiveData.observe(viewLifecycleOwner){
            timeStrings = it.toMutableList()
            setRings()
        }

        viewModel.isModeratorLiveData.observe(viewLifecycleOwner){
            isModerator = it.isModerator
            if (isModerator){
                toolbar.menu.clear()
                toolbar.inflateMenu(R.menu.rings_toolbar_menu)
                updateToolbarColors()
            }
            for (i in ringViews){
                i.firstTime.isClickable = isModerator
                i.secondTime.isClickable = isModerator
            }
        }

        toolbar.menu.clear()
        if (isModerator) {
            toolbar.inflateMenu(R.menu.rings_toolbar_menu)
            updateToolbarColors()
        }
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_saverings){
                var adb = AlertDialog.Builder(activity)
                var dialogView = layoutInflater.inflate(R.layout.popup_save_homework_dialog, null)
                dialogView.findViewById<TextView>(R.id.description).text = "Сохранить звонки"
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
                    updateRings()
                    viewModel.saveRings(timeStrings)
                    dialog.cancel()
                }
            }
            true
        }
    }

    fun updateRings(){
        for (i in timeStrings.indices){
            if (i % 2 == 0){
                timeStrings[i] = ringViews[i/2].firstTime.text.toString()
            }else{
                timeStrings[i] = ringViews[i/2].secondTime.text.toString()
            }
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

    override fun onDestroyView() {
        updateRings()

        viewModel.rings = timeStrings
        viewModel.setLiveData()
        super.onDestroyView()
    }

    fun setRings(){
        for (i in timeStrings.indices){
            if (timeStrings[i] != "") {
                if (i % 2 == 0) {
                    ringViews[i / 2].setFirstTime(timeStrings[i])
                } else {
                    ringViews[i / 2].setSecondTime(timeStrings[i])
                }
            }
        }
    }
}