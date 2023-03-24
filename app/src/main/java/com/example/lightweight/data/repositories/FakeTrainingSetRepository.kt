package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.db.entities.Workout

class FakeTrainingSetRepository : TrainingSetRepositoryInterface {

    var workouts = mutableListOf<Workout>()

    var exerciseInstances = mutableListOf<ExerciseInstance>()

    private val trainingSets = mutableListOf<TrainingSet>()
    private val observableTrainingSets = MutableLiveData<List<TrainingSet>>(trainingSets)

    private fun refreshLiveData() {
        observableTrainingSets.postValue(trainingSets)
    }

    override suspend fun insert(trainingSet: TrainingSet) {
        trainingSets.add(trainingSet)
        refreshLiveData()
    }

    override suspend fun delete(trainingSet: TrainingSet) {
        trainingSets.remove(trainingSet)
        refreshLiveData()
    }

    override suspend fun update(trainingSet: TrainingSet) {
        trainingSets.removeIf { it.trainingSetID == trainingSet.trainingSetID }
        trainingSets.add(trainingSet)
        refreshLiveData()
    }

    override suspend fun updateIsPR(trainingSetID: Int?, isPR: Int) {
        for (trainingSet in trainingSets.filter { it.trainingSetID == trainingSetID }) {
            trainingSet.isPR = isPR == 1
            refreshLiveData()
            return
        }
    }

    override suspend fun updateNote(trainingSetID: Int?, note: String?) {
        for (trainingSet in trainingSets.filter { it.trainingSetID == trainingSetID }) {
            trainingSet.note = note
            refreshLiveData()
            return
        }
    }

    override suspend fun decrementTrainingSetNumbersAbove(
        exerciseInstanceID: Int?,
        trainingSetNumber: Int
    ) {
        for (trainingSet in trainingSets.filter {
            it.exerciseInstanceID == exerciseInstanceID && it.trainingSetNumber > trainingSetNumber
        }) {
            trainingSet.trainingSetNumber--
            refreshLiveData()
        }
    }

    override fun getAllTrainingSets(): LiveData<List<TrainingSet>> {
        return observableTrainingSets
    }

    override fun getTrainingSetDatesOfExerciseIsPR(
        exerciseID: Int?,
        isPR: Int
    ): LiveData<List<String>> {
        val isPrBool = isPR == 1
        val datesAndReps = arrayListOf<Pair<String, Int>>()

        for (workout in workouts) {
            for (exerciseInstance in exerciseInstances.filter {
                it.workoutID == workout.workoutID &&
                it.exerciseID == exerciseID
            }) {
                for (trainingSet in trainingSets.filter {
                    it.exerciseInstanceID == exerciseInstance.exerciseInstanceID &&
                            it.isPR == isPrBool
                }) {
                    datesAndReps.add(Pair(workout.date, trainingSet.reps))
                }
            }
        }

        // Sort dates by corresponding rep count
        datesAndReps.sortedBy { it.second }
        val dates = arrayListOf<String>()
        for (pair in datesAndReps) {
            dates.add(pair.first)
        }

        return MutableLiveData(dates)
    }

    override fun getTrainingSetsOfExerciseAndIsPR(
        exerciseID: Int?,
        isPR: Int
    ): LiveData<List<TrainingSet>> {
        val isPrBool = isPR == 1
        val trainingSetsToReturn = arrayListOf<TrainingSet>()

        for (exerciseInstance in exerciseInstances.filter {
            it.exerciseID == exerciseID
        }) {
            for (trainingSet in trainingSets.filter {
                it.exerciseInstanceID == exerciseInstance.exerciseInstanceID &&
                it.isPR == isPrBool
            }) {
                trainingSetsToReturn.add(trainingSet)
            }
        }

        return MutableLiveData(trainingSetsToReturn.sortedBy { it.reps })
    }

    override fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>> {
//        return MutableLiveData(trainingSets.filter {
//            it.exerciseInstanceID == exerciseInstanceID
//        })
        val sets = arrayListOf<TrainingSet>()
        for (trainingSet in trainingSets) {
            if (trainingSet.exerciseInstanceID == exerciseInstanceID) {
                sets.add(trainingSet)
            }
        }
        sets.sortBy { it.trainingSetNumber }
        return MutableLiveData(sets)
    }

    override fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?): List<TrainingSet> {
        return trainingSets.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }
    }

    override fun getTrainingSetsOfExerciseRepsIsPR(
        exerciseID: Int?,
        reps: Int,
        isPR: Int
    ): LiveData<List<TrainingSet>> {
        TODO("Joins are going to be a pain")
    }

    override fun getTrainingSetsOfExerciseFewerReps(
        exerciseID: Int?,
        reps: Int
    ): LiveData<List<TrainingSet>> {
        TODO("Joins are going to be a pain")
    }

    override fun getTrainingSetsOfExercise(exerciseID: Int?): LiveData<List<TrainingSet>> {
        val trainingSetDatePairs = arrayListOf<Pair<TrainingSet, String>>()

        for (exerciseInstance in exerciseInstances.filter { it.exerciseID == exerciseID }) {
            for (workout in workouts.filter { it.workoutID == exerciseInstance.workoutID }) {
                for (trainingSet in trainingSets.filter {
                    it.exerciseInstanceID == exerciseInstance.exerciseInstanceID
                }) {
                    trainingSetDatePairs.add(Pair(trainingSet, workout.date))
                }
            }
        }

        // Sort by date then trainingSetNumber
        trainingSetDatePairs.sortWith(
            compareBy(
                {
                    it.second
                },
                {
                    it.first.trainingSetNumber
                }
            )
        )
        val trainingSetsOfExercise = arrayListOf<TrainingSet>()
        for (pair in trainingSetDatePairs) {
            trainingSetsOfExercise.add(pair.first)
        }

        return MutableLiveData(trainingSetsOfExercise)
    }
}