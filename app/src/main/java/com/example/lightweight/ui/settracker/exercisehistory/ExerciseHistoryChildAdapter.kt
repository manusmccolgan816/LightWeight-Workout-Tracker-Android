package com.example.lightweight.ui.settracker.exercisehistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet

class ExerciseHistoryChildAdapter(
    var trainingSets: List<TrainingSet>,
) : RecyclerView.Adapter<ExerciseHistoryChildAdapter.ExerciseHistoryChildViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var imageViewTrophy: ImageView
    private lateinit var textViewSetNumber: TextView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ExerciseHistoryChildViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_history_child, parent, false)
        this.parent = parent
        return ExerciseHistoryChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHistoryChildViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        imageViewTrophy = holder.itemView.findViewById(R.id.image_view_trophy)
        textViewSetNumber = holder.itemView.findViewById(R.id.text_view_training_set_number)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        textViewSetNumber.text = curTrainingSet.trainingSetNumber.toString()
        textViewSetWeight.text = curTrainingSet.weight.toString()
        textViewSetReps.text = curTrainingSet.reps.toString()

        // Display the trophy if the set is a PR
        if (curTrainingSet.isPR) imageViewTrophy.visibility = View.VISIBLE
        else imageViewTrophy.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class ExerciseHistoryChildViewHolder(view: View): RecyclerView.ViewHolder(view)
}