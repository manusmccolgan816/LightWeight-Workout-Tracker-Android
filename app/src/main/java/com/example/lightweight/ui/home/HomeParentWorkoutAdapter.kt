package com.example.lightweight.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeParentWorkoutAdapter(
    var exerciseInstances: List<ExerciseInstance>,
    var exerciseNames: ArrayList<String>,
    private val fragment: Fragment
) : RecyclerView.Adapter<HomeParentWorkoutAdapter.HomeParentWorkoutViewHolder>(), KodeinAware {

    override val kodein by kodein(fragment.requireContext())
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by fragment.viewModels { trainingSetFactory }

    private lateinit var parent: ViewGroup

    private lateinit var textViewExerciseName: TextView
    private lateinit var recyclerViewTrainingSets: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeParentWorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_exercise_instance, parent, false)

        this.parent = parent

        return HomeParentWorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeParentWorkoutViewHolder, position: Int) {
        val curExerciseInstance = exerciseInstances[position]
        Log.d(null, "onBindViewHolder at position $position")

        textViewExerciseName = holder.itemView.findViewById(R.id.text_view_exercise_name)
        recyclerViewTrainingSets = holder.itemView.findViewById(R.id.recycler_view_training_sets)

        // Set the text to the name of the exercise
        if (exerciseNames.size > position) textViewExerciseName.text = exerciseNames[position]

        // Set up the child recycler view
        val homeChildWorkoutAdapter = HomeChildWorkoutAdapter(listOf())
        recyclerViewTrainingSets.layoutManager = LinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewTrainingSets.adapter = homeChildWorkoutAdapter

        trainingSetViewModel.getTrainingSetsOfExerciseInstance(
            curExerciseInstance.exerciseInstanceID).observe(fragment.viewLifecycleOwner) {
                homeChildWorkoutAdapter.trainingSets = it
                homeChildWorkoutAdapter.notifyItemRangeChanged(0, it.size)
                Log.d(null, "Data set changed at position $position")
            }
    }

    override fun getItemCount(): Int {
        return exerciseInstances.size
    }

    inner class HomeParentWorkoutViewHolder(setView: View): RecyclerView.ViewHolder(setView)
}