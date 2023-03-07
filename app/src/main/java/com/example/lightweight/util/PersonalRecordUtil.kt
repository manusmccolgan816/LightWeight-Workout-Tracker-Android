package com.example.lightweight.util

import com.example.lightweight.data.db.entities.TrainingSet

object PersonalRecordUtil {

    /**
     * Returns a pair where the first value is a Boolean to indicate whether a set with the given
     * weight and reps is a PR among the prSets that were logged on prDates. The second value is an
     * ArrayList of the training set IDs of the training sets that would no longer be PRs after
     * adding a training set with the given weight and reps.
     */
    fun calculateIsPR(
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
}