package com.example.lightweight.ui.exercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectExerciseFragment : Fragment(R.layout.fragment_select_exercise), KodeinAware {

    override val kodein by kodein()
    private val factory: ExerciseViewModelFactory by instance()

    private lateinit var recyclerViewExercises: RecyclerView
    private lateinit var fabAddExercise: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: ExerciseViewModel by viewModels { factory }
        val adapter = ExerciseItemAdapter(listOf(), viewModel)

        recyclerViewExercises = view.findViewById(R.id.recycler_view_exercises)
        recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExercises.adapter = adapter

        viewModel.getAllExercises().observe(viewLifecycleOwner) {
            adapter.exercises = it
            adapter.notifyDataSetChanged()
        }

//        fabAddExercise = view.findViewById(R.id.fab_add_exercise)
//        fabAddExercise.setOnClickListener {
//            // Display the add category dialog
//            AddExerciseDialog(
//                requireContext(),
//                fun(exercise: Exercise) { viewModel.insert(exercise) }
//            ).show()
//        }
    }

}