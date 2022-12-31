package com.example.lightweight.ui.settracker.logsets

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import org.kodein.di.generic.allInstances
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

    private lateinit var adapter: TrainingSetItemAdapter
    private var isAdapterSetup = false

    private var exerciseID: Int? = null
    private var workoutID: Int? = null
    private var exerciseInstanceID: Int? = null
    private lateinit var selectedDate: String

    private lateinit var editTextWeight: EditText
    private lateinit var editTextNumReps: EditText
    private lateinit var buttonClearSet: Button
    private lateinit var buttonSaveSet: Button
    private lateinit var recyclerViewTrainingSets: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID // Set exerciseID from the SetTrackerActivity arg
        selectedDate = act.args.selectedDate // Set selectedDate from the SetTrackerActivity arg

        val ref = this.activity
        lifecycleScope.launch(Dispatchers.IO) {
            // If a workout exists for the selected date...
            if (workoutViewModel.getWorkoutOfDate(selectedDate) != null) {
                // Get a reference to the workoutID
                workoutID = workoutViewModel.getWorkoutOfDate(selectedDate)!!.workoutID

                // If an exercise instance exists for the selected date and exercise
                if (exerciseInstanceViewModel.getExerciseInstance(workoutID, exerciseID) != null) {
                    // Get a reference to the exerciseInstanceID
                    exerciseInstanceID = exerciseInstanceViewModel
                        .getExerciseInstance(workoutID, exerciseID)!!.exerciseInstanceID

                    // Set up the adapter outside of the coroutine
                    ref?.runOnUiThread {
                        setupAdapter()
                        isAdapterSetup = true
                    }
                }
            }
        }

        editTextWeight = view.findViewById(R.id.edit_text_weight)
        editTextNumReps = view.findViewById(R.id.edit_text_num_reps)
        buttonClearSet = view.findViewById(R.id.button_clear_set)
        buttonSaveSet = view.findViewById(R.id.button_save_set)
        recyclerViewTrainingSets = view.findViewById(R.id.recycler_view_training_sets)

        adapter = TrainingSetItemAdapter(listOf(), trainingSetViewModel)
        recyclerViewTrainingSets.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingSets.adapter = adapter

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
            val ref = this.activity

            lifecycleScope.launch(Dispatchers.IO) {
                // If no workout exists for the selected date...
                if (workoutViewModel.getWorkoutOfDate(selectedDate) == null) {
                    // ...create a new workout
                    val insWorkoutJob = workoutViewModel.insert(Workout(selectedDate, null))
                    insWorkoutJob.join() // Wait for the insertion to finish
                    workoutID = workoutViewModel.getWorkoutOfDate(selectedDate)!!.workoutID
                }

                // If no exercise instance exists for the selected date and exercise...
                if (exerciseInstanceViewModel.getExerciseInstance(workoutID, exerciseID)
                    == null) {
                    // ...create a new exercise instance
                    val instExerciseInstanceJob = exerciseInstanceViewModel
                        .insert(ExerciseInstance(workoutID, exerciseID, null))
                    instExerciseInstanceJob.join() // Wait for the insertion to finish
                    exerciseInstanceID = exerciseInstanceViewModel
                        .getExerciseInstance(workoutID, exerciseID)!!.exerciseInstanceID
                }

                ref?.runOnUiThread {
                    var isPR = false
                    val setsToCompare = trainingSetViewModel.getTrainingSetsOfExerciseAndReps(exerciseID, reps)
                    setsToCompare.observe(viewLifecycleOwner) {
                        // It is a PR if this is the first set of the given exercise and number of
                        // reps
                        if (it.isEmpty()) isPR = true
                        // It is a PR if the weight is higher than the previous heaviest set of the
                        // given exercise and number of reps
                        else if (weight > it[0].weight) {
                            trainingSetViewModel.setIsPRFalse(it[0].trainingSetID)
                            isPR = true
                        }
                        // Insert the new training set
                        val trainingSet = TrainingSet(exerciseInstanceID, weight, reps, null,
                            isPR)
                        trainingSetViewModel.insert(trainingSet)

                        // Stop observing for changes in the data
                        setsToCompare.removeObservers(viewLifecycleOwner)
                    }
                }

                // If the adapter has not already been set up...
                if (!isAdapterSetup) {
                    // Set up the adapter outside of the coroutine
                    ref?.runOnUiThread {
                        setupAdapter()
                        isAdapterSetup = true
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Set saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            Toast.makeText(requireContext(), "Enter weight and reps", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Returns a Boolean indicating whether the weight and reps given would constitute a personal
     * record in a TrainingSet of the exercise. This overwrites the old TrainingSet's isPR value if
     * it is surpassed.
     */
//    private fun isPRRemovePrevious(weight: Float, reps: Int) : Boolean {
//        // Store all of the given exercise's sets of the given rep count
//        //val setsToCompare = trainingSetViewModel.getTrainingSetsOfExerciseAndReps(exerciseID, reps)
//        var setsToCompare: List<TrainingSet>
//        trainingSetViewModel.getTrainingSetsOfExerciseAndReps(exerciseID, reps).observe(viewLifecycleOwner) {
//            setsToCompare = it
//        }
//
//        // If the new weight is heavier than the previous heaviest...
//        if (weight > setsToCompare[0].weight) {
//            // Set the old set's isPR value to false
//            trainingSetViewModel.setIsPRFalse(setsToCompare[0].trainingSetID)
//
//            return true
//        }
//
//        return false
//    }

    private fun setupAdapter() {
        trainingSetViewModel.getTrainingSetsOfExerciseInstance(exerciseInstanceID)
            .observe(viewLifecycleOwner) {
                adapter.trainingSets = it
                adapter.notifyDataSetChanged()
            }
    }
}