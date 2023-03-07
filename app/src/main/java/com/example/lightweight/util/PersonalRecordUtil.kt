package com.example.lightweight.util

import com.example.lightweight.data.db.entities.TrainingSet
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object PersonalRecordUtil {

    /**
     * Returns a pair where the first value is a Boolean to indicate whether a set with the given
     * weight and reps is a PR among the prSets that were logged on prDates. The second value is an
     * ArrayList of the training set IDs of the training sets that would no longer be PRs after
     * adding a training set with the given weight and reps.
     */
    fun calculateIsNewSetPr(
        reps: Int,
        weight: Float,
        selectedDate: String,
        prSets: List<TrainingSet>,
        prDates: List<String>
    ): Pair<Boolean, ArrayList<Int?>> {
        var isPR = false
        val noLongerPRTrainingSetIDs = arrayListOf<Int?>()

        // If there are no PRs (and so no training sets) of this exercise...
        if (prSets.isEmpty()) {
            isPR = true
        } else {
            val repWeightMappings: HashMap<Int, Float> = HashMap()

            // If the new set has more reps than any other of the exercise...
            if (prSets[prSets.size - 1].reps < reps) {
                isPR = true
            }

            // Loop through each PR (arranged from lowest to highest reps)
            var count = 0
            loop@ for (i in prSets) {
                // If i has a higher rep count than the new set...
                if (i.reps > reps) {
                    // ...if the new set has a higher weight AND there is not a
                    // PR with the same number of reps...
                    if (i.weight < weight && repWeightMappings[reps] == null) {
                        isPR = true
                    }
                    break@loop
                }
                // If i has fewer reps and lower or equal weight than the new
                // set OR i has the same number of reps and a lower weight than
                // the new set OR i is the same as the new set and the new set
                // is of an earlier date...
                if ((i.reps < reps && i.weight <= weight)
                    || (i.reps == reps && i.weight < weight)
                    || (i.reps == reps && i.weight == weight
                            && selectedDate < prDates[count])
                ) {
                    // Keep a record of i's ID as it's PR status should be removed
                    noLongerPRTrainingSetIDs.add(i.trainingSetID)

                    isPR = true
                }
                // Add i's reps and weight to the HashMap
                repWeightMappings[i.reps] = i.weight
                count++
            }
        }

        return Pair(isPR, noLongerPRTrainingSetIDs)
    }

    /**
     * Returns an ArrayList of the IDs of the training sets that would be new PRs were
     * curTrainingSet deleted.
     */
    fun getNewPrSetsAfterDeletion(
        curTrainingSet: TrainingSet,
        prSets: List<TrainingSet>,
        sameRepSets: List<TrainingSet>,
        lowerRepSets: List<TrainingSet>
    ): ArrayList<Int?> {
        val newPRTrainingSetIDs = arrayListOf<Int?>()
        val updatedPRSets: LinkedList<TrainingSet> = LinkedList()

        // Populate updatedPRSets with all PR sets but the one to be deleted
        for (i in prSets) {
            if (i.trainingSetID != curTrainingSet.trainingSetID) {
                updatedPRSets.add(i)
            }
        }

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
                addToUpdatedPRSets(updatedPRSets, possiblePRSet)
                newPRTrainingSetIDs.add(possiblePRSet.trainingSetID)
            }
        }

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
                        addToUpdatedPRSets(
                            updatedPRSets,
                            lowerRepSets[i]
                        )
                        newPRTrainingSetIDs.add(lowerRepSets[i].trainingSetID)
                    }
                }
            }
        }

        return newPRTrainingSetIDs
    }

    /**
     * Adds newSet to the correct index of updatedPRSets, which is ordered by reps (ascending).
     */
    fun addToUpdatedPRSets(updatedPRSets: LinkedList<TrainingSet>, newSet: TrainingSet)
            : LinkedList<TrainingSet> {
        // Add the now PR training set to the correct index of
        // updatedPRSets
        if (updatedPRSets.isEmpty()) {
            updatedPRSets.add(newSet)
            return updatedPRSets
        }

        loop@ for (i in updatedPRSets.indices) {
            if (i + 1 != updatedPRSets.size) {
                if (newSet.reps > updatedPRSets[i].reps
                    && newSet.reps < updatedPRSets[i + 1].reps
                ) {
                    updatedPRSets.add(i + 1, newSet)
                    break@loop
                } else if (newSet.reps < updatedPRSets[i].reps) {
                    updatedPRSets.addFirst(newSet)
                    break@loop
                }
            }
            updatedPRSets.addLast(newSet)
        }
        return updatedPRSets
    }
}