package com.example.lightweight.ui.settracker.logsets

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
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashSet

class TrainingSetItemAdapter(
    var trainingSets: List<TrainingSet>,
    private val trainingSetViewModel: TrainingSetViewModel,
    private val exerciseID: Int?,
    private val selectedDate: String,
    private val fragment: Fragment
) : RecyclerView.Adapter<TrainingSetItemAdapter.TrainingSetItemViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var imageViewTrophy: ImageView
    private lateinit var imageViewSetOptions: ImageView
    private lateinit var textViewSetNumber: TextView
    private lateinit var textViewSetWeight: TextView
    private lateinit var textViewSetReps: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingSetItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_set, parent, false)
        this.parent = parent
        return TrainingSetItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingSetItemViewHolder, position: Int) {
        val curTrainingSet = trainingSets[position]

        imageViewTrophy = holder.itemView.findViewById(R.id.image_view_trophy)
        imageViewSetOptions = holder.itemView.findViewById(R.id.image_view_training_set_options)
        textViewSetNumber = holder.itemView.findViewById(R.id.text_view_training_set_number)
        textViewSetWeight = holder.itemView.findViewById(R.id.text_view_training_set_weight)
        textViewSetReps = holder.itemView.findViewById(R.id.text_view_training_set_reps)

        textViewSetNumber.text = curTrainingSet.trainingSetNumber.toString()
        textViewSetWeight.text = curTrainingSet.weight.toString()
        textViewSetReps.text = curTrainingSet.reps.toString()
        // Display the trophy if the set is a PR
        if (curTrainingSet.isPR) imageViewTrophy.visibility = View.VISIBLE
        else imageViewTrophy.visibility = View.INVISIBLE

        imageViewSetOptions.setOnClickListener {
            // Create the popup menu anchored to the training set item
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.training_set_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_training_set -> {
                        // Display the edit training set dialog
                        EditTrainingSetDialog(parent.context, curTrainingSet,
                            fun (weight: Float, reps: Int) {
                                updateOtherSetsIsPR(curTrainingSet, weight, reps)
                            }).show()
                        true
                    }
                    R.id.menu_item_delete_training_set -> {
                        // If the set to be deleted is a PR, another set may become a PR
                        if (curTrainingSet.isPR) {
                            val prTrainingSetsObs = trainingSetViewModel
                                .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
                            prTrainingSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->
                                val updatedPRSets: LinkedList<TrainingSet> = LinkedList()

                                // Populate updatedPRSets with all PR sets but the one to be deleted
                                for (i in prSets) {
                                    if (i.trainingSetID != curTrainingSet.trainingSetID) {
                                        updatedPRSets.add(i)
                                    }
                                }

                                val sameRepSetsObs = trainingSetViewModel
                                    .getTrainingSetsOfExerciseRepsIsPR(exerciseID,
                                        curTrainingSet.reps, 0)
                                sameRepSetsObs.observe(fragment.viewLifecycleOwner) { sameRepSets ->
                                    // If at least one other set of the same number of reps exists...
                                    if (sameRepSets.isNotEmpty()) {
                                        var makePR = true
                                        val possiblePRSet = sameRepSets[0]

                                        loop@ for (i in updatedPRSets) {
                                            // If i has a higher rep count than the heaviest non-PR
                                            // set of the same rep count as the set to be deleted...
                                            if (i.reps > possiblePRSet.reps) {
                                                // ...it will be made a PR if i has a lower weight
                                                makePR = i.weight < possiblePRSet.weight
                                                break@loop
                                            }
                                        }

                                        if (makePR) {
                                            // Add the now PR training set to the correct index of
                                            // updatedPRSets
                                            loop@ for (i in updatedPRSets.indices) {
                                                if (i + 1 != updatedPRSets.size) {
                                                    if (possiblePRSet.reps > updatedPRSets[i].reps
                                                        && possiblePRSet.reps < updatedPRSets[i + 1].reps) {
                                                        updatedPRSets.add(i + 1, possiblePRSet)
                                                        break@loop
                                                    }
                                                    else if (possiblePRSet.reps < updatedPRSets[i].reps) {
                                                        updatedPRSets.addFirst(possiblePRSet)
                                                        break@loop
                                                    }
                                                }
                                                updatedPRSets.addLast(possiblePRSet)
                                            }
                                            if (updatedPRSets.isEmpty()) {
                                                updatedPRSets.add(possiblePRSet)
                                            }
                                            trainingSetViewModel
                                                .updateIsPR(possiblePRSet.trainingSetID, 1)
                                        }
                                    }
                                    sameRepSetsObs.removeObservers(fragment.viewLifecycleOwner)

                                    val lowerRepSetsObs =
                                        trainingSetViewModel.getTrainingSetsOfExerciseFewerReps(
                                            exerciseID, curTrainingSet.reps)
                                    lowerRepSetsObs.observe(fragment.viewLifecycleOwner) { lowerRepSets ->
                                        val repValues: HashSet<Int> = HashSet()
                                        var reps: Int
                                        var weight: Float
                                        // Iterate through each training set with fewer reps than the
                                        // set to be deleted
                                        for (i in lowerRepSets.indices) {
                                            reps = lowerRepSets[i].reps
                                            weight = lowerRepSets[i].weight

                                            // If this is the first set of the given rep count that is
                                            // being iterated through...
                                            if (!repValues.contains(reps)) {
                                                repValues.add(reps)
                                                // If the set is not a PR...
                                                if (!lowerRepSets[i].isPR) {
                                                    var makePR1 = true
                                                    // Check if the training set should be made a PR
                                                    loop@ for (j in updatedPRSets.size - 1 downTo 0) {
                                                        if (updatedPRSets[j].reps <= reps) break@loop
                                                        if (updatedPRSets[j].weight >= weight) {
                                                            makePR1 = false
                                                            break@loop
                                                        }
                                                    }
                                                    if (makePR1) {
                                                        trainingSetViewModel.updateIsPR(
                                                            lowerRepSets[i].trainingSetID, 1)
                                                    }
                                                }
                                            }
                                        }
                                        lowerRepSetsObs.removeObservers(fragment.viewLifecycleOwner)
                                    }
                                }
                                prTrainingSetsObs.removeObservers(fragment.viewLifecycleOwner)
                            }
                        }

                        // Decrement trainingSetNumber of all sets after curTrainingSet
                        trainingSetViewModel
                            .decrementTrainingSetNumbersAbove(curTrainingSet.exerciseInstanceID,
                                curTrainingSet.trainingSetNumber)
                        // Delete the training set
                        trainingSetViewModel.delete(curTrainingSet)
                        Toast.makeText(parent.context, "Set has been deleted",
                            Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    private fun updateOtherSetsIsPR(curTrainingSet: TrainingSet, weight: Float, reps: Int) {
        //region Deal with the PR status of other sets
        if (curTrainingSet.isPR) {
            val prTrainingSetsObs = trainingSetViewModel
                .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
            prTrainingSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->
                val updatedPRSets: LinkedList<TrainingSet> = LinkedList()

                // Populate updatedPRSets with all PR sets but the one to be deleted
                for (i in prSets) {
                    if (i.trainingSetID != curTrainingSet.trainingSetID) {
                        updatedPRSets.add(i)
                    }
                }

                Log.d(null, "curTrainingSet isPR: ${curTrainingSet.isPR}")
                val sameRepSetsObs = trainingSetViewModel
                    .getTrainingSetsOfExerciseRepsIsPR(exerciseID,
                        curTrainingSet.reps, 0)
                sameRepSetsObs.observe(fragment.viewLifecycleOwner) { sameRepSets ->
                    // If at least one other set of the same number of reps exists...
                    if (sameRepSets.isNotEmpty()) {
                        var makePR = true
                        val possiblePRSet = sameRepSets[0]

                        loop@ for (i in updatedPRSets) {
                            // If i has a higher rep count than the heaviest non-PR
                            // set of the same rep count as the set to be deleted...
                            if (i.reps > possiblePRSet.reps) {
                                // ...it will be made a PR if i has a lower weight
                                makePR = i.weight < possiblePRSet.weight
                                break@loop
                            }
                        }

                        if (makePR) {
                            // Add the now PR training set to the correct index of
                            // updatedPRSets
                            loop@ for (i in updatedPRSets.indices) {
                                if (i + 1 != updatedPRSets.size) {
                                    if (possiblePRSet.reps > updatedPRSets[i].reps
                                        && possiblePRSet.reps < updatedPRSets[i + 1].reps) {
                                        updatedPRSets.add(i + 1, possiblePRSet)
                                        break@loop
                                    }
                                    else if (possiblePRSet.reps < updatedPRSets[i].reps) {
                                        updatedPRSets.addFirst(possiblePRSet)
                                        break@loop
                                    }
                                }
                                updatedPRSets.addLast(possiblePRSet)
                            }
                            if (updatedPRSets.isEmpty()) {
                                updatedPRSets.add(possiblePRSet)
                            }

                            trainingSetViewModel
                                .updateIsPR(possiblePRSet.trainingSetID, 1)
                        }
                    }
                    sameRepSetsObs.removeObservers(fragment.viewLifecycleOwner)
                    Log.d(null, "Removed sameRepSetsObs")

                    val lowerRepSetsObs =
                        trainingSetViewModel.getTrainingSetsOfExerciseFewerReps(
                            exerciseID, curTrainingSet.reps)
                    lowerRepSetsObs.observe(fragment.viewLifecycleOwner) { lowerRepSets ->
                        val repValues: HashSet<Int> = HashSet()
                        var reps1: Int
                        var weight1: Float
                        // Iterate through each training set with fewer reps than the
                        // set to be deleted
                        for (i in lowerRepSets.indices) {
                            reps1 = lowerRepSets[i].reps
                            weight1 = lowerRepSets[i].weight

                            // If this is the first set of the given rep count that is
                            // being iterated through...
                            if (!repValues.contains(reps1)) {
                                repValues.add(reps1)
                                // If the set is not a PR...
                                if (!lowerRepSets[i].isPR) {
                                    var makePR1 = true
                                    // Check if the training set should be made a PR
                                    loop@ for (j in updatedPRSets.size - 1 downTo 0) {
                                        if (updatedPRSets[j].reps <= reps1) break@loop
                                        if (updatedPRSets[j].weight >= weight1) {
                                            makePR1 = false
                                            break@loop
                                        }
                                    }
                                    if (makePR1) {
                                        trainingSetViewModel.updateIsPR(
                                            lowerRepSets[i].trainingSetID, 1)
                                    }
                                }
                            }
                        }
                        lowerRepSetsObs.removeObservers(fragment.viewLifecycleOwner)
                        Log.d(null, "Removed lowerRepSetsObs")

                        reviewIsPRAndUpdateTrainingSet(curTrainingSet, weight, reps)
                    }
                    prTrainingSetsObs.removeObservers(fragment.viewLifecycleOwner)
                    Log.d(null, "Removed prTrainingSetsObs")
                }
            }
        }
        else {
            reviewIsPRAndUpdateTrainingSet(curTrainingSet, weight, reps)
        }
        //endregion
    }

    /**
     * Check if the training set should be a PR and update it accordingly.
     */
    private fun reviewIsPRAndUpdateTrainingSet(curTrainingSet: TrainingSet, weight: Float, reps: Int) {
        fragment.lifecycleScope.launch(Dispatchers.IO) {
            Log.d(null, "Updating PR status")
            val tsJob = trainingSetViewModel.updateIsPR(curTrainingSet.trainingSetID, 0)
            tsJob.join()

            val ref = fragment.activity
            ref?.runOnUiThread {
                //region Check if updated set is PR
                var isPR = false
                // Get the PR sets of the current exercise by observing a LiveData
                val previousPRSetsObs = trainingSetViewModel
                    .getTrainingSetsOfExerciseAndIsPR(exerciseID, 1)
                previousPRSetsObs.observe(fragment.viewLifecycleOwner) { prSets ->

                    // Get the dates of the PR sets by observing a LiveData
                    val prDatesObs = trainingSetViewModel
                        .getTrainingSetDatesOfExerciseIsPR(exerciseID, 1)
                    prDatesObs.observe(fragment.viewLifecycleOwner) { prDates ->

                        // If there are no PRs (and so no training sets) of this exercise...
                        if (prSets.isEmpty()) {
                            // ...the new set will be a PR
                            isPR = true
                        }
                        else {
                            val repWeightMappings: HashMap<Int, Float> = HashMap()
                            // If the new set has more reps than any other of the exercise...
                            if (prSets[prSets.size -1].reps < reps) {
                                // ...it will be a PR
                                isPR = true
                            }

                            // Loop through each PR (arranged from lowest to highest reps)
                            var count = 0
                            loop@ for (i in prSets) {
                                // If i has a higher rep count than the new set...
                                if (i.reps > reps) {
                                    // ...if the new set has a higher weight AND there is not a
                                    // PR with the same number of reps...
                                    if (i.weight < weight && repWeightMappings.get(reps) == null) {
                                        // ...the new set is a PR
                                        isPR = true
                                    }
                                    break@loop
                                }
                                // If i has fewer reps and lower or equal weight than the new
                                // set OR i has the same number of reps and a higher weight than
                                // the new set OR i is the same as the new set and the new set
                                // is of an earlier date...
                                if ((i.reps < reps && i.weight <= weight)
                                    || (i.reps == reps && i.weight < weight)
                                    || (i.reps == reps && i.weight == weight
                                            && selectedDate < prDates[count])) {
                                    // ...i is no longer a PR
                                    trainingSetViewModel.updateIsPR(i.trainingSetID, 0)
                                    // The new set is a PR
                                    isPR = true
                                }
                                // Add i's reps and weight to the HashMap
                                repWeightMappings.put(i.reps, i.weight)
                                count++
                            }
                        }

                        prDatesObs.removeObservers(fragment.viewLifecycleOwner)

                        // Update the training set
                        val trainingSet = TrainingSet(curTrainingSet.exerciseInstanceID,
                            curTrainingSet.trainingSetNumber, weight, reps, null, isPR)
                        trainingSet.trainingSetID = curTrainingSet.trainingSetID
                        trainingSetViewModel.update(trainingSet)
                    }

                    previousPRSetsObs.removeObservers(fragment.viewLifecycleOwner)
                }
                //endregion
            }
        }
    }

    override fun getItemCount(): Int {
        return trainingSets.size
    }

    inner class TrainingSetItemViewHolder(setView: View): RecyclerView.ViewHolder(setView)
}