package com.example.lightweight.ui.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise

class ExerciseItemAdapter(
    var exercises: List<Exercise>,
    private val viewModel: ExerciseViewModel
) : RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder>() {

    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ExerciseItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        this.parent = parent
        return ExerciseItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        val curExercise = exercises[position]

    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    inner class ExerciseItemViewHolder(exerciseView: View): RecyclerView.ViewHolder(exerciseView)
}