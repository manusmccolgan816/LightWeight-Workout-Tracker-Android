package com.example.lightweight.ui.settracker.logsets

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LogSetsFragment() : Fragment(R.layout.fragment_log_sets), KodeinAware {

    override val kodein by kodein()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()

    private val date = "18/12/2022" // TODO Change this to the selected date, perhaps as an arg

    private lateinit var editTextWeight: EditText
    private lateinit var editTextNumReps: EditText
    private lateinit var buttonClearSet: Button
    private lateinit var buttonSaveSet: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
        val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
            exerciseInstanceFactory
        }
        val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }

        editTextWeight = view.findViewById(R.id.edit_text_weight)
        editTextNumReps = view.findViewById(R.id.edit_text_num_reps)

        buttonClearSet = view.findViewById(R.id.button_clear_set)
        buttonClearSet.setOnClickListener {
            editTextWeight.text.clear()
            editTextNumReps.text.clear()
            Toast.makeText(requireContext(), "Text cleared", Toast.LENGTH_SHORT).show()
        }

        buttonSaveSet = view.findViewById(R.id.button_save_set)
        buttonSaveSet.setOnClickListener {
            val weight = editTextWeight.text.toString().toFloatOrNull()
            val reps = editTextNumReps.text.toString().toIntOrNull()
            // If weight and reps have been input...
            if (weight != null && reps != null) {
                var workout = Workout(date, null) // TODO Change this of course

                lifecycleScope.launch(Dispatchers.IO) {
                    // Create the workout if none exists for the given date
                    if (workoutViewModel.getWorkoutOfDate(date) == null) {
                        workout = Workout(date, null)
                        workoutViewModel.insert(workout)
                    }

                    val setTrackerActivity: SetTrackerActivity = activity as SetTrackerActivity
                    val exerciseID = setTrackerActivity.args.exerciseID
                    // TODO Change this of course
                    var exerciseInstance: ExerciseInstance = ExerciseInstance(1,
                        1, null)
                    // Create an exercise instance if none exists for the given date and exercise
                    if (exerciseInstanceViewModel.getExerciseInstance(workout.workoutID, exerciseID)
                            == null) {
                        exerciseInstance = ExerciseInstance(workout.workoutID, exerciseID, null)
                        exerciseInstanceViewModel.insert(exerciseInstance)
                    }
                    // TODO Change this (isPR should be checked)
                    val trainingSet = TrainingSet(exerciseInstance.exerciseInstanceID, weight, reps,
                        null, false)
                    trainingSetViewModel.insert(trainingSet)
                }
                Toast.makeText(requireContext(), "Set saved", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Enter weight and reps", Toast.LENGTH_SHORT).show()
            }
        }
    }
}