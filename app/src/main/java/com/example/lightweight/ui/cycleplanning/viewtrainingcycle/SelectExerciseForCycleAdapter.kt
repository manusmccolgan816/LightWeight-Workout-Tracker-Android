package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise

class SelectExerciseForCycleAdapter(
    var exercises: List<Exercise>,
    val selectExercise: (Exercise) -> Unit
) : RecyclerView.Adapter<SelectExerciseForCycleAdapter.SelectExerciseForCycleViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var textViewExerciseName: TextView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectExerciseForCycleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_for_cycle, parent, false)
        this.parent = parent
        return SelectExerciseForCycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectExerciseForCycleViewHolder, position: Int) {
        val curExercise = exercises[position]

        textViewExerciseName = holder.itemView.findViewById(R.id.text_view_exercise_name)

        textViewExerciseName.text = curExercise.exerciseName

        textViewExerciseName.setOnClickListener {
            selectExercise(curExercise)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    inner class SelectExerciseForCycleViewHolder(view: View) : RecyclerView.ViewHolder(view)
}