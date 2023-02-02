package com.example.lightweight.ui.calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R

class CalendarViewHolder(
    itemView: View,
    private val onItemListener: CalendarAdapter.OnItemListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val dayOfMonth: TextView = itemView.findViewById(R.id.text_view_cell_day)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        onItemListener.onItemClick(absoluteAdapterPosition, dayOfMonth.text.toString())
    }
}