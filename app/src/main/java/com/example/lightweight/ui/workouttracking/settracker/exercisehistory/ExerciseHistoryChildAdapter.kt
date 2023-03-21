package com.example.lightweight.ui.workouttracking.settracker.exercisehistory

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet

class ExerciseHistoryChildAdapter(
    var trainingSets: List<TrainingSet>,
    private val fragment: Fragment
) : RecyclerView.Adapter<ExerciseHistoryChildAdapter.ExerciseHistoryChildViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var parent: ViewGroup

    private lateinit var imageViewTrophy: ImageView
    private lateinit var imageViewSetNote: ImageView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ExerciseHistoryChildViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_history_child, parent, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.requireContext())
        this.parent = parent

        return ExerciseHistoryChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHistoryChildViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        imageViewTrophy = holder.itemView.findViewById(R.id.image_view_trophy)
        imageViewSetNote = holder.itemView.findViewById(R.id.image_view_training_set_note)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        if (sharedPreferences.getString("unit", "kg") == "kg") {
            textViewSetWeight.text = fragment.resources.getString(
                R.string.string_weight_kg,
                curTrainingSet.weight.toString()
            )
        } else {
            textViewSetWeight.text = fragment.resources.getString(
                R.string.string_weight_lbs,
                curTrainingSet.weight.toString()
            )
        }

        if (curTrainingSet.reps == 1) {
            // Display the number followed by 'rep'
            textViewSetReps.text = fragment.resources.getString(
                R.string.string_number_rep,
                curTrainingSet.reps
            )
        } else {
            // Display the number followed by 'reps'
            textViewSetReps.text = fragment.resources.getString(
                R.string.string_number_reps,
                curTrainingSet.reps
            )
        }

        // If the training set has a note...
        if (!curTrainingSet.note.isNullOrBlank()) {
            // ...display the text bubble icon
            imageViewSetNote.visibility = View.VISIBLE
        }

        // Display the trophy if the set is a PR
        if (curTrainingSet.isPR) imageViewTrophy.visibility = View.VISIBLE
        else imageViewTrophy.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class ExerciseHistoryChildViewHolder(view: View) : RecyclerView.ViewHolder(view)
}