package com.example.lightweight.ui.workouttracking.settracker.exercisehistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ExerciseHistoryParentAdapter(
    var idDateMappings: Map<Int?, String>,
    private val fragment: Fragment,
    private val trainingSetViewModel: TrainingSetViewModel
) : RecyclerView.Adapter<ExerciseHistoryParentAdapter.ExerciseHistoryParentViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var textViewDate: TextView
    private lateinit var recyclerViewTrainingSets: RecyclerView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseHistoryParentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_history_parent, parent, false)

        this.parent = parent

        return ExerciseHistoryParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHistoryParentViewHolder, position: Int) {
        // Convert the map to an array to get the value at each index one-by-one
        val curDate = idDateMappings.values.toTypedArray()[position]
        val curID = idDateMappings.keys.toTypedArray()[position]

        textViewDate = holder.itemView.findViewById(R.id.text_view_date)
        recyclerViewTrainingSets = holder.itemView.findViewById(R.id.recycler_view_training_sets)

        // Get the date formatted as (for example) 24 Jan 2023
        val date: String? =
            LocalDate.parse(curDate).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        textViewDate.text = date.toString()

        // Set up the child recycler view
        val adapter = ExerciseHistoryChildAdapter(listOf(), fragment)
        recyclerViewTrainingSets.layoutManager = LinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.VERTICAL, false
        )
        recyclerViewTrainingSets.adapter = adapter

        trainingSetViewModel.getTrainingSetsOfExerciseInstance(curID)
            .observe(fragment.viewLifecycleOwner) {
                adapter.trainingSets = it
                adapter.notifyItemRangeChanged(0, it.size)
            }
    }

    override fun getItemCount(): Int {
        return idDateMappings.size
    }

    inner class ExerciseHistoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view)
}