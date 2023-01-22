package com.example.lightweight.ui.cycleplanning

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle

class TrainingCycleItemAdapter(
    var cycles: List<Cycle>,
    private val fragment: SelectTrainingCycleFragment
) : RecyclerView.Adapter<TrainingCycleItemAdapter.TrainingCycleItemViewHolder>() {

    private val logTag = "TrainingCycleItemAdapter"

    private lateinit var parent: ViewGroup

    private lateinit var textViewTrainingCycleName: TextView
    private lateinit var textViewTrainingCycleDesc: TextView
    private lateinit var imageViewTrainingCycleOptions: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingCycleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_cycle, parent, false)
        this.parent = parent
        return TrainingCycleItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingCycleItemViewHolder, position: Int) {
        val curCycle = cycles[position]

        textViewTrainingCycleName = holder.itemView.findViewById(R.id.text_view_training_cycle_name)
        textViewTrainingCycleDesc = holder.itemView.findViewById(R.id.text_view_training_cycle_desc)
        imageViewTrainingCycleOptions =
            holder.itemView.findViewById(R.id.image_view_training_cycle_options)

        // Display the training cycle name
        textViewTrainingCycleName.text = curCycle.cycleName
        // Display the training cycle description
        textViewTrainingCycleDesc.text = curCycle.description

        imageViewTrainingCycleOptions.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return cycles.size
    }

    inner class TrainingCycleItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}