package com.example.lightweight.ui.settracker.logsets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.trainingset.TrainingSetViewModel

class TrainingSetItemAdapter(
    var trainingSets: List<TrainingSet>,
    private val trainingSetViewModel: TrainingSetViewModel,
) : RecyclerView.Adapter<TrainingSetItemAdapter.TrainingSetItemViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var textViewSetNumber: TextView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingSetItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_set, parent, false)
        this.parent = parent
        return TrainingSetItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingSetItemViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        textViewSetNumber = holder.itemView.findViewById(R.id.text_view_training_set_number)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        textViewSetNumber.text = (position + 1).toString()

    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class TrainingSetItemViewHolder(setView: View): RecyclerView.ViewHolder(setView)
}