package com.example.lightweight.ui.workouttracking.settracker.logsets

import android.content.SharedPreferences
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.util.PersonalRecordUtil.calculateIsNewSetPr
import com.example.lightweight.util.PersonalRecordUtil.getNewPrSetsOnDeletion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingSetItemAdapter(
    var trainingSets: List<TrainingSet>,
    private val exerciseID: Int?,
    private val selectedDate: String,
    private val viewModel: LogSetsViewModel,
    private val fragment: Fragment
) : RecyclerView.Adapter<TrainingSetItemAdapter.TrainingSetItemViewHolder>() {

    private val logTag = "TrainingSetItemAdapter"

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var parent: ViewGroup

    private lateinit var imageViewSetNote: ImageView
    private lateinit var imageViewTrophy: ImageView
    private lateinit var imageViewSetOptions: ImageView
    private lateinit var textViewSetNumber: TextView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingSetItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_set, parent, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.requireContext())
        this.parent = parent

        return TrainingSetItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingSetItemViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        imageViewSetNote = holder.itemView.findViewById(R.id.image_view_training_set_note)
        imageViewTrophy = holder.itemView.findViewById(R.id.image_view_trophy)
        imageViewSetOptions = holder.itemView.findViewById(R.id.image_view_training_set_options)
        textViewSetNumber = holder.itemView.findViewById(R.id.text_view_training_set_number)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        textViewSetNumber.text = curTrainingSet.trainingSetNumber.toString()
        if (sharedPreferences.getString("unit", "kg") == "kg") {
            // Display the weight followed by kg
            textViewSetWeight.text = fragment.resources.getString(
                R.string.string_weight_kg,
                curTrainingSet.weight.toString()
            )
        } else {
            // Display the weight followed by lbs
            textViewSetWeight.text = fragment.resources.getString(
                R.string.string_weight_lbs,
                curTrainingSet.weight.toString()
            )
        }
        if (curTrainingSet.reps == 1) {
            // Display the number followed by 'rep'
            textViewSetReps.text = fragment.resources.getString(
                R.string.string_number_rep,
                curTrainingSet.reps
            )
        } else {
            // Display the number followed by 'reps'
            textViewSetReps.text = fragment.resources.getString(
                R.string.string_number_reps,
                curTrainingSet.reps
            )
        }

        // If the training set has a note...
        if (!curTrainingSet.note.isNullOrBlank()) {
            // ...use the filled comment image
            imageViewSetNote.setImageResource(R.drawable.ic_baseline_filled_comment_24)
        }
        // If the set has no note...
        else {
            // ...use the empty comment image
            imageViewSetNote.setImageResource(R.drawable.ic_baseline_empty_comment_24)
        }

        // Display the trophy if the set is a PR
        if (curTrainingSet.isPR) imageViewTrophy.visibility = View.VISIBLE
        else imageViewTrophy.visibility = View.INVISIBLE

        imageViewSetNote.setOnClickListener {
            TrainingSetNoteDialog(parent.context, curTrainingSet,
                fun(trainingSetID: Int?, note: String?) {
                    viewModel.updateTrainingSetNote(trainingSetID, note)
                }).show()
        }

        imageViewSetOptions.setOnClickListener {
            // Create the popup menu anchored to the training set item
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.training_set_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_training_set -> {
                        // Display the edit training set dialog
                        EditTrainingSetDialog(
                            parent.context,
                            curTrainingSet,
                            fun(weight: Float, reps: Int) {
                                updateOtherSetsIsPR(curTrainingSet, weight, reps)
                            }).show()
                        true
                    }
                    R.id.menu_item_delete_training_set -> {
                        // If the set to be deleted is a PR, another set may become a PR
                        if (curTrainingSet.isPR) {
                            val prTrainingSetsObs = viewModel
                                .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
                            prTrainingSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->
                                prTrainingSetsObs.removeObservers(fragment.viewLifecycleOwner)

                                val sameRepSetsObs = viewModel
                                    .getTrainingSetsOfExerciseRepsIsPR(
                                        exerciseID,
                                        curTrainingSet.reps,
                                        0
                                    )
                                sameRepSetsObs.observe(fragment.viewLifecycleOwner) { sameRepSets ->
                                    sameRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                                    val lowerRepSetsObs =
                                        viewModel.getTrainingSetsOfExerciseFewerReps(
                                            exerciseID, curTrainingSet.reps
                                        )
                                    lowerRepSetsObs.observe(fragment.viewLifecycleOwner) { lowerRepSets ->
                                        lowerRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                                        val newPrIds = getNewPrSetsOnDeletion(
                                            curTrainingSet,
                                            prSets,
                                            sameRepSets,
                                            lowerRepSets
                                        )

                                        // Set isPr to true for all training sets that are now PRs
                                        for (id in newPrIds) {
                                            viewModel.updateTrainingSetIsPR(id, 1)
                                        }
                                    }
                                }
                            }
                        }

                        // If this is the only set in the exercise instance...
                        if (this.itemCount <= 1) {
                            fragment.lifecycleScope.launch(Dispatchers.IO) {
                                Log.d(logTag, "About to delete exercise instance")
                                // Delete the exercise instance
                                val curExerciseInstance = viewModel
                                    .getExerciseInstanceOfID(curTrainingSet.exerciseInstanceID)
                                val delExInstanceJob =
                                    viewModel.deleteExerciseInstance(curExerciseInstance)

                                val updateExInstanceNumbersJob =
                                    viewModel.decrementExerciseInstanceNumbersOfWorkoutAfter(
                                        curExerciseInstance.workoutID,
                                        curExerciseInstance.exerciseInstanceNumber
                                    )

                                // Wait until the deletion is completed
                                delExInstanceJob.join()
                                updateExInstanceNumbersJob.join()

                                val workout = viewModel.getWorkoutOfDate(selectedDate)

                                val exInstOfWorkoutObs = viewModel
                                    .getExerciseInstancesOfWorkout(workout?.workoutID)
                                Log.d(logTag, "workoutID: ${workout?.workoutID}")

                                val ref = fragment.activity
                                ref?.runOnUiThread {
                                    exInstOfWorkoutObs.observe(fragment.viewLifecycleOwner) { exInstancesOfWorkout ->
                                        Log.d(logTag, "Entered exInstOfWorkoutObs")
                                        if (exInstancesOfWorkout.isEmpty()) {
                                            Log.d(
                                                logTag,
                                                "No exercise instances of workout found"
                                            )
                                            // Delete the workout if it has no exercise
                                            // instances
                                            viewModel.deleteWorkoutOfID(workout?.workoutID)
                                        }
                                        exInstOfWorkoutObs.removeObservers(fragment.viewLifecycleOwner)
                                    }
                                }
                            }
                        } else {
                            // Decrement trainingSetNumber of all sets after curTrainingSet
                            viewModel.decrementTrainingSetNumbersAbove(
                                curTrainingSet.exerciseInstanceID,
                                curTrainingSet.trainingSetNumber
                            )

                            // Delete the training set
                            viewModel.deleteTrainingSet(curTrainingSet)
                        }

                        Toast.makeText(
                            parent.context, "Set has been deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    /**
     * Update the PR status of any sets of the same or lower rep count than curTrainingSet,
     * considering the set will be updated and could make other sets PRs.
     */
    private fun updateOtherSetsIsPR(curTrainingSet: TrainingSet, weight: Float, reps: Int) {
        if (curTrainingSet.isPR) {
            val prTrainingSetsObs = viewModel
                .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
            prTrainingSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->
                val sameRepSetsObs = viewModel
                    .getTrainingSetsOfExerciseRepsIsPR(
                        exerciseID,
                        curTrainingSet.reps,
                        0
                    )
                sameRepSetsObs.observe(fragment.viewLifecycleOwner) { sameRepSets ->
                    sameRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                    val lowerRepSetsObs =
                        viewModel.getTrainingSetsOfExerciseFewerReps(
                            exerciseID, curTrainingSet.reps
                        )
                    lowerRepSetsObs.observe(fragment.viewLifecycleOwner) { lowerRepSets ->
                        lowerRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                        val newPrIds = getNewPrSetsOnDeletion(
                            curTrainingSet,
                            prSets,
                            sameRepSets,
                            lowerRepSets
                        )

                        // Set isPr to true for all training sets that are now PRs
                        for (id in newPrIds) {
                            viewModel.updateTrainingSetIsPR(id, 1)
                        }

                        reviewIsPRAndUpdateTrainingSet(curTrainingSet, weight, reps)
                    }
                }
                prTrainingSetsObs.removeObservers(fragment.viewLifecycleOwner)
            }
        } else {
            reviewIsPRAndUpdateTrainingSet(curTrainingSet, weight, reps)
        }
    }

    /**
     * Check if the training set should be a PR and update it accordingly.
     */
    private fun reviewIsPRAndUpdateTrainingSet(
        curTrainingSet: TrainingSet,
        weight: Float,
        reps: Int
    ) {
        fragment.lifecycleScope.launch(Dispatchers.IO) {
            Log.d(null, "Updating PR status")
            val tsJob = viewModel.updateTrainingSetIsPR(curTrainingSet.trainingSetID, 0)
            tsJob.join()

            val ref = fragment.activity
            ref?.runOnUiThread {
                var isPR: Boolean
                // Get the PR sets of the current exercise by observing a LiveData
                val previousPRSetsObs = viewModel
                    .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
                previousPRSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->

                    // Get the dates of the PR sets by observing a LiveData
                    val prDatesObs = viewModel
                        .getTrainingSetDatesOfExerciseIsPR(exerciseID, 1)
                    prDatesObs.observe(fragment.viewLifecycleOwner) { prDates ->
                        prDatesObs.removeObservers(fragment.viewLifecycleOwner)

                        val pair = calculateIsNewSetPr(reps, weight, selectedDate, prSets, prDates)

                        isPR = pair.first
                        val noLongerPrIds = pair.second

                        // Set isPr to false for all training sets that will no longer be a PR
                        for (id in noLongerPrIds) {
                            viewModel.updateTrainingSetIsPR(id, 0)
                        }

                        // Update the training set
                        val trainingSet = TrainingSet(
                            curTrainingSet.exerciseInstanceID,
                            curTrainingSet.trainingSetNumber,
                            weight,
                            reps,
                            curTrainingSet.note,
                            isPR
                        )
                        trainingSet.trainingSetID = curTrainingSet.trainingSetID
                        viewModel.updateTrainingSet(trainingSet)
                    }

                    previousPRSetsObs.removeObservers(fragment.viewLifecycleOwner)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class TrainingSetItemViewHolder(setView: View) : RecyclerView.ViewHolder(setView)
}