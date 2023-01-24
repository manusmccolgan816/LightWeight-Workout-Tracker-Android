package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.data.db.entities.CycleDayCategory
import com.example.lightweight.data.db.entities.CycleDayExercise

class TrainingCycleDayAdapter(
    var cycleDays: List<CycleDay>,
    var cycleDayCategories: List<CycleDayCategory>,
    var cycleDayExercises: List<CycleDayExercise>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var parent: ViewGroup

    private val nameLayout = 0
    private val categoryLayout = 1
    private val exerciseLayout = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent

        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            nameLayout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_name, parent, false)
                TrainingCycleDayNameViewHolder(view)
            }
            categoryLayout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_category, parent, false)
                TrainingCycleDayCategoryViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_exercise, parent, false)
                TrainingCycleDayExerciseViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        TODO()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class TrainingCycleDayNameViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class TrainingCycleDayCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class TrainingCycleDayExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}