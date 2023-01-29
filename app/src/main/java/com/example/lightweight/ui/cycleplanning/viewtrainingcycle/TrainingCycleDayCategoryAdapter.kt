package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDayCategory

class TrainingCycleDayCategoryAdapter(
    var idNameMappings: Map<Int?, String>,
    private val fragment: Fragment
) : RecyclerView.Adapter<TrainingCycleDayCategoryAdapter.TrainingCycleDayCategoryViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var textViewTrainingCycleDayCategory: TextView

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
        val curCategoryName = idNameMappings.values.toTypedArray()[position]
        val curCycleDayCategoryID = idNameMappings.keys.toTypedArray()[position]

        textViewTrainingCycleDayCategory =
            holder.itemView.findViewById(R.id.text_view_training_cycle_day_category)

        textViewTrainingCycleDayCategory.text = curCategoryName
    }

    override fun getItemCount(): Int {
        return idNameMappings.size
    }

    inner class TrainingCycleDayCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}