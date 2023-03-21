package com.example.lightweight.ui.workouttracking.calendar

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val selectedDayOfMonthPosition: Int?,
    private val todayPosition: Int?,
    private val workoutPositions: ArrayList<Int>,
    private val onItemListener: OnItemListener,
    private val fragment: Fragment
) : RecyclerView.Adapter<CalendarViewHolder>() {

    private val logTag = "CalenderAdapter"

    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_calendar_cell, parent, false)
        this.parent = parent
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]

        holder.dayOfMonth.background = null

        if (selectedDayOfMonthPosition != null && position == selectedDayOfMonthPosition) {
            if (workoutPositions.contains(selectedDayOfMonthPosition)) {
                // Set the background for a selected date that has a workout
                holder.dayOfMonth.background = ContextCompat.getDrawable(
                    parent.context,
                    R.drawable.selected_date_workout_background
                )
            } else {
                // Set the background for a selected date that has no workout
                holder.dayOfMonth.background = ContextCompat.getDrawable(
                    parent.context,
                    R.drawable.selected_date_no_workout_background
                )
            }
        }

        if (workoutPositions.contains(position)) {
            if (position != selectedDayOfMonthPosition) {
                // Set the background for a non-selected date that has a workout
                holder.dayOfMonth.background = ContextCompat.getDrawable(
                    parent.context,
                    R.drawable.not_selected_date_workout_background
                )
            }
        }

        if (todayPosition != null && position == todayPosition) {
            // Embolden today's date
            holder.dayOfMonth.typeface = Typeface.DEFAULT_BOLD
            // Set the text colour to the theme's icon colour
            holder.dayOfMonth.setTextColor(
                fragment.resources.getColor(
                    R.color.iconColor,
                    fragment.requireContext().theme
                )
            )
            Log.d(logTag, "Emboldening position $position")
        } else {
            holder.dayOfMonth.typeface = Typeface.DEFAULT
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String)
    }
}