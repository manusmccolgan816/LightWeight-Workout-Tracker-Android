package com.example.lightweight.ui.settracker.logsets

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.settracker.SetTrackerActivity
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workout.WorkoutViewModel
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import com.example.lightweight.util.PersonalRecordUtil.calculateIsNewSetPr
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LogSetsFragment : Fragment(R.layout.fragment_log_sets), KodeinAware {

    private val logTag = "LogSetsFragment"

    override val kodein by kodein()
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()

    private val exerciseViewModel: ExerciseViewModel by viewModels { exerciseFactory }
    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetViewModel: TrainingSetViewModel by viewModels { trainingSetFactory }

    private lateinit var adapter: TrainingSetItemAdapter
    private var isAdapterSetup = false

    // Used when pressing increment and decrement buttons
    private val repIncrement = 1
    private var weightIncrement: Float = 0F

    private var exerciseID: Int? = null
    private var workoutID: Int? = null
    private var exerciseInstanceID: Int? = null
    private lateinit var selectedDate: String

    private lateinit var editTextWeight: EditText
    private lateinit var imageViewReduceWeight: ImageView
    private lateinit var imageViewAddWeight: ImageView
    private lateinit var editTextNumReps: EditText
    private lateinit var imageViewReduceReps: ImageView
    private lateinit var imageViewAddReps: ImageView
    private lateinit var buttonClearSet: Button
    private lateinit var buttonSaveSet: Button
    private lateinit var recyclerViewTrainingSets: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act: SetTrackerActivity = this.activity as SetTrackerActivity
        exerciseID = act.args.exerciseID // Set exerciseID from the SetTrackerActivity arg
        selectedDate = act.args.selectedDate // Set selectedDate from the SetTrackerActivity arg

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // The weight increment is taken from shared preferences
        weightIncrement = sharedPreferences.getString("weightIncrement", "2.5")
            ?.toFloat()!!

        val ref = this.activity
        lifecycleScope.launch(Dispatchers.IO) {
            val exercise = exerciseViewModel.getExerciseOfID(exerciseID)
            ref?.runOnUiThread {
                // Set the action bar title
                activity?.title = exercise.exerciseName
            }

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
        imageViewReduceWeight = view.findViewById(R.id.image_view_reduce_weight)
        imageViewAddWeight = view.findViewById(R.id.image_view_add_weight)
        editTextNumReps = view.findViewById(R.id.edit_text_num_reps)
        imageViewReduceReps = view.findViewById(R.id.image_view_reduce_reps)
        imageViewAddReps = view.findViewById(R.id.image_view_add_reps)
        buttonClearSet = view.findViewById(R.id.button_clear_set)
        buttonSaveSet = view.findViewById(R.id.button_save_set)
        recyclerViewTrainingSets = view.findViewById(R.id.recycler_view_training_sets)

        adapter = TrainingSetItemAdapter(
            listOf(), trainingSetViewModel, exerciseID, selectedDate, this
        )
        recyclerViewTrainingSets.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingSets.adapter = adapter

        imageViewReduceWeight.setOnClickListener {
            val weight = editTextWeight.text.toString().toFloatOrNull()

            if (weight != null) {
                if (weight > weightIncrement) {
                    editTextWeight.setText((weight - weightIncrement).toString())
                } else {
                    editTextWeight.setText("0")
                }
            }
        }

        imageViewAddWeight.setOnClickListener {
            val weight = editTextWeight.text.toString().toFloatOrNull()

            if (weight == null) {
                editTextWeight.setText(weightIncrement.toString())
            } else {
                editTextWeight.setText((weight + weightIncrement).toString())
            }
        }

        imageViewReduceReps.setOnClickListener {
            val reps = editTextNumReps.text.toString().toIntOrNull()

            if (reps != null) {
                if (reps > repIncrement) {
                    editTextNumReps.setText((reps - repIncrement).toString())
                } else {
                    editTextNumReps.setText("")
                }
            }
        }

        imageViewAddReps.setOnClickListener {
            val reps = editTextNumReps.text.toString().toIntOrNull()

            if (reps == null) {
                editTextNumReps.setText(repIncrement.toString())
            } else {
                editTextNumReps.setText((reps + repIncrement).toString())
            }
        }

        buttonClearSet.setOnClickListener {
            editTextWeight.text.clear()
            editTextNumReps.text.clear()
        }

        buttonSaveSet.setOnClickListener {
            saveTrainingSet()
        }
    }

    private fun saveTrainingSet() {
        val weight = editTextWeight.text.toString().toFloatOrNull()
        val reps = editTextNumReps.text.toString().toIntOrNull()

        if (reps != null && reps <= 0) {
            Toast.makeText(requireContext(), "Reps must not be 0", Toast.LENGTH_SHORT).show()
            return
        }
        if ((weight != null) && (reps != null)) {
            val ref = this.activity

            lifecycleScope.launch(Dispatchers.IO) {
                // If no workout exists for the selected date...
                if (workoutViewModel.getWorkoutOfDate(selectedDate) == null) {
                    // Create a new workout
                    val insWorkoutJob = workoutViewModel.insert(Workout(selectedDate, null))
                    insWorkoutJob.join() // Wait for the insertion to finish

                    workoutID = workoutViewModel.getWorkoutOfDate(selectedDate)!!.workoutID
                }

                // If no exercise instance exists for the selected date and exercise...
                if (exerciseInstanceViewModel.getExerciseInstance(workoutID, exerciseID) == null) {
                    // Get the exercise instance number to assign for ordering purposes
                    val eiNumber = exerciseInstanceViewModel
                        .getExerciseInstancesOfWorkoutNoLiveData(workoutID).size + 1

                    // Create and insert a new exercise instance
                    val instExerciseInstanceJob = exerciseInstanceViewModel.insert(
                        ExerciseInstance(workoutID, exerciseID, eiNumber, null)
                    )
                    instExerciseInstanceJob.join() // Wait for the insertion to finish

                    exerciseInstanceID = exerciseInstanceViewModel
                        .getExerciseInstance(workoutID, exerciseID)!!.exerciseInstanceID
                }

                ref?.runOnUiThread {
                    var isPR: Boolean
                    // Get the PR sets of the current exercise by observing a LiveData
                    val previousPRSetsObs = trainingSetViewModel
                        .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
                    previousPRSetsObs.observe(viewLifecycleOwner) { prSets ->

                        // Get the dates of the PR sets by observing a LiveData
                        val prDatesObs = trainingSetViewModel
                            .getTrainingSetDatesOfExerciseIsPR(exerciseID, 1)
                        prDatesObs.observe(viewLifecycleOwner) { prDates ->
                            prDatesObs.removeObservers(viewLifecycleOwner)

                            val pair =
                                calculateIsNewSetPr(reps, weight, selectedDate, prSets, prDates)

                            isPR = pair.first
                            val noLongerPrIds = pair.second

                            // Set isPr to false for all training sets that will no longer be a PR
                            for (id in noLongerPrIds) {
                                trainingSetViewModel.updateIsPR(id, 0)
                            }

                            // Insert the new training set
                            val trainingSet = TrainingSet(
                                exerciseInstanceID,
                                adapter.itemCount + 1, weight, reps, null, isPR
                            )
                            trainingSetViewModel.insert(trainingSet)
                        }

                        previousPRSetsObs.removeObservers(viewLifecycleOwner)
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
            }
        } else {
            Toast.makeText(requireContext(), "Enter weight and reps", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapter() {
        trainingSetViewModel.getTrainingSetsOfExerciseInstance(exerciseInstanceID)
            .observe(viewLifecycleOwner) {
                Log.d(logTag, "Training sets data changed")
                adapter.trainingSets = it
                adapter.notifyDataSetChanged()

                if (it.isNotEmpty()) {
                    // Set the text of the edit text fields to the last set completed in the
                    // exercise instance
                    editTextWeight.setText(it[it.size - 1].weight.toString())
                    editTextNumReps.setText(it[it.size - 1].reps.toString())
                }
            }
    }
}