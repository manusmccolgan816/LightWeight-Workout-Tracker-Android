package com.example.lightweight.ui.workouttracking.settracker.exercisehistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.workouttracking.settracker.SetTrackerActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ExerciseHistoryFragment : Fragment(R.layout.fragment_exercise_history), KodeinAware {

    override val kodein by kodein()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }

    private lateinit var adapter: ExerciseHistoryParentAdapter

    private var exerciseID: Int? = null

    private lateinit var recyclerViewExerciseInstances: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID // Set exerciseID from the SetTrackerActivity arg

        recyclerViewExerciseInstances = view.findViewById(R.id.recycler_view_exercise_instances)

        adapter = ExerciseHistoryParentAdapter(mapOf(), this)
        recyclerViewExerciseInstances.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExerciseInstances.adapter = adapter

        exerciseInstanceViewModel.getExerciseInstancesAndDatesOfExercise(exerciseID)
            .observe(viewLifecycleOwner) {
                adapter.idDateMappings = it
                adapter.notifyItemRangeChanged(0, it.size)
            }
    }
}