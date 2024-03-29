package com.example.lightweight.ui.workouttracking.settracker.exercisehistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.settracker.SetTrackerActivity
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel

class ExerciseHistoryFragment(
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel,
    private val trainingSetViewModel: TrainingSetViewModel
) : Fragment(R.layout.fragment_exercise_history) {

    private lateinit var adapter: ExerciseHistoryParentAdapter

    private var exerciseID: Int? = null

    private lateinit var recyclerViewExerciseInstances: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.activity is SetTrackerActivity) {
            val act: SetTrackerActivity = this.activity as SetTrackerActivity
            exerciseID = act.args.exerciseID // Set exerciseID from the SetTrackerActivity arg
        } else {
            // Launching the fragment in isolation will require arguments to be passed directly
            val args: ExerciseHistoryFragmentArgs by navArgs()
            exerciseID = args.exerciseID
        }

        recyclerViewExerciseInstances = view.findViewById(R.id.recycler_view_exercise_instances)

        adapter = ExerciseHistoryParentAdapter(mapOf(), this, trainingSetViewModel)
        recyclerViewExerciseInstances.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExerciseInstances.adapter = adapter

        exerciseInstanceViewModel.getExerciseInstancesAndDatesOfExercise(exerciseID)
            .observe(viewLifecycleOwner) {
                adapter.idDateMappings = it
                adapter.notifyItemRangeChanged(0, it.size)
            }
    }
}