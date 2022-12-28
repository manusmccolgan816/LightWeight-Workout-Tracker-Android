package com.example.lightweight.ui.settracker.logsets

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.settracker.SetTrackerActivity
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workout.WorkoutViewModel
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LogSetsFragment : Fragment(R.layout.fragment_log_sets), KodeinAware {

    override val kodein by kodein()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()

    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }

    private var exerciseID: Int? = null
    private lateinit var selectedDate: String

    private lateinit var editTextWeight: EditText
    private lateinit var editTextNumReps: EditText
    private lateinit var buttonClearSet: Button
    private lateinit var buttonSaveSet: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID // Set exerciseID from the SetTrackerActivity arg
        selectedDate = act.args.selectedDate // Set selectedDate from the SetTrackerActivity arg

        editTextWeight = view.findViewById(R.id.edit_text_weight)
        editTextNumReps = view.findViewById(R.id.edit_text_num_reps)
        buttonClearSet = view.findViewById(R.id.button_clear_set)
        buttonSaveSet = view.findViewById(R.id.button_save_set)

        buttonClearSet.setOnClickListener {
            editTextWeight.text.clear()
            editTextNumReps.text.clear()
            Toast.makeText(requireContext(), "Text cleared", Toast.LENGTH_SHORT).show()
        }

        buttonSaveSet.setOnClickListener {
            saveTrainingSet()
        }
    }

    private fun saveTrainingSet() {
        val weight = editTextWeight.text.toString().toFloatOrNull()
        val reps = editTextNumReps.text.toString().toIntOrNull()
        // If weight and reps have been input...
        if (weight != null && reps != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                // If no workout exists for the selected date...
                if (workoutViewModel.getWorkoutOfDate(selectedDate) == null) {
                    // ...create a new workout
                    val insWorkoutJob = workoutViewModel.insert(Workout(selectedDate, null))
                    insWorkoutJob.join() // Wait for the insertion to finish
                }
                val workoutID = workoutViewModel.getWorkoutOfDate(selectedDate)!!.workoutID

                // If no exercise instance exists for the selected date and exercise...
                if (exerciseInstanceViewModel.getExerciseInstance(workoutID, exerciseID)
                        == null) {
                    // ...create a new exercise instance
                    val instExerciseInstanceJob = exerciseInstanceViewModel
                        .insert(ExerciseInstance(workoutID, exerciseID, null))
                    instExerciseInstanceJob.join() // Wait for the insertion to finish
                }
                val exerciseInstanceID = exerciseInstanceViewModel
                    .getExerciseInstance(workoutID, exerciseID)!!.exerciseInstanceID

                // TODO Change this (isPR should be checked)
                val trainingSet = TrainingSet(exerciseInstanceID, weight, reps,
                    null, false)
                trainingSetViewModel.insert(trainingSet)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Set saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            Toast.makeText(requireContext(), "Enter weight and reps", Toast.LENGTH_SHORT).show()
        }
    }
}