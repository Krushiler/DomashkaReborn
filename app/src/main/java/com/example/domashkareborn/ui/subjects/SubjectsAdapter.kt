package com.example.domashkareborn.ui.subjects

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domashkareborn.R
import com.example.domashkareborn.ui.subjects.itemtouchhelper.ItemTouchHelperAdapter
import com.example.domashkareborn.ui.subjects.itemtouchhelper.ItemTouchHelperViewHolder
import com.example.domashkareborn.ui.subjects.itemtouchhelper.OnStartDragListener
import java.util.Collections.swap


class SubjectsAdapter(private val subjects: MutableList<String>, onStartDragListener: OnStartDragListener) :
    RecyclerView.Adapter<SubjectsAdapter.ItemViewHolder>(), ItemTouchHelperAdapter {

    private val mDragStartListener: OnStartDragListener = onStartDragListener

    public interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_row_item, parent, false)

        return ItemViewHolder(view)
    } 

    public var itemClickListener: onItemClickListener? = null

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.subjectNameTV.text = subjects[position]

        holder.handleView.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false
            }
        })

        if (itemClickListener != null){
            holder.itemView.setOnClickListener {

                itemClickListener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    public fun getSubjectItem(position:Int):String{
        return subjects[position]
    }

    public fun getAllSubjects():List<String>{
        return subjects
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        swap(subjects, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        subjects.removeAt(position)
        notifyItemRemoved(position)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemTouchHelperViewHolder {
        val subjectNameTV: TextView
        val handleView: ImageView
        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

        init {
            subjectNameTV = itemView.findViewById<View>(R.id.subjectName) as TextView
            handleView = itemView.findViewById<View>(R.id.handle) as ImageView
        }
    }

}