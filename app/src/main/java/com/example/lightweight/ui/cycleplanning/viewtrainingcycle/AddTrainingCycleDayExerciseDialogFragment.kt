package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.category.CategoryViewModelFactory
import com.example.lightweight.ui.workouttracking.selectexercise.AddExerciseDialog
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AddTrainingCycleDayExerciseDialogFragment(
    private val categoryID: Int?,
    val addCycleDayExercise: (Exercise) -> Unit
) : DialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val categoryFactory: CategoryViewModelFactory by instance()
    private val categoryViewModel: CategoryViewModel by viewModels { categoryFactory }
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val exerciseViewModel: ExerciseViewModel by viewModels { exerciseFactory }

    private lateinit var recyclerViewExercises: RecyclerView
    private lateinit var fabAddExercise: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.dialog_add_training_cycle_day_exercise, container, false)

        recyclerViewExercises = view.findViewById(R.id.recycler_view_exercises)
        fabAddExercise = view.findViewById(R.id.fab_add_exercise)

        val adapter = SelectExerciseForCycleAdapter(
            listOf(),
            fun(exercise: Exercise) {
                addCycleDayExercise(exercise)
                dismiss()
            },
            this
        )
        recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExercises.adapter = adapter

        exerciseViewModel.getExercisesOfCategory(categoryID).observe(viewLifecycleOwner) {
            adapter.exercises = it
            adapter.notifyDataSetChanged()
        }

        fabAddExercise.setOnClickListener {
            val getCategoryObs = categoryViewModel.getCategoryOfIDObs(categoryID)
            getCategoryObs.observe(viewLifecycleOwner) { category ->
                // Display the add category dialog
                AddExerciseDialog(
                    requireContext(),
                    category,
                    fun(exercise: Exercise) { exerciseViewModel.insert(exercise) }
                ).show()

                getCategoryObs.removeObservers(viewLifecycleOwner)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        // Set the width and height as XML values do not work
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}