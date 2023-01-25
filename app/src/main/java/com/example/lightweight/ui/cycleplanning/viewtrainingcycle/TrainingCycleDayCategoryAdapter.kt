package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDayCategory

class TrainingCycleDayCategoryAdapter(
    var cycleDayCategories: List<CycleDayCategory>,
) : RecyclerView.Adapter<TrainingCycleDayCategoryAdapter.TrainingCycleDayCategoryViewHolder>() {

    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrainingCycleDayCategoryViewHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_cycle_day_category, parent, false)
        return TrainingCycleDayCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingCycleDayCategoryViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return cycleDayCategories.size
    }

    inner class TrainingCycleDayCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}