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

    private val observableTrainingSetDatesOfExerciseIsPR = MutableLiveData<List<String>>()
    private var trainingSetDatesOfExerciseIsPRParamExerciseID: Int? = null
    private var trainingSetDatesOfExerciseIsPRParamIsPR: Int? = null

    private val observableTrainingSetsOfExerciseAndIsPR = MutableLiveData<List<TrainingSet>>()
    private var trainingSetsOfExerciseAndIsPRParamExerciseID: Int? = null
    private var trainingSetsOfExerciseAndIsPRParamIsPR: Int? = null

    private val observableTrainingSetsOfExerciseInstance = MutableLiveData<List<TrainingSet>>()
    private var trainingSetsOfExerciseInstanceParam: Int? = null

    private val observableTrainingSetsOfExerciseRepsIsPR = MutableLiveData<List<TrainingSet>>()
    private var trainingSetsOfExerciseRepsIsPRParamExerciseID: Int? = null
    private var trainingSetsOfExerciseRepsIsPRParamReps: Int? = null
    private var trainingSetsOfExerciseRepsIsPRParamIsPR: Int? = null

    private val observableTrainingSetsOfExerciseFewerReps = MutableLiveData<List<TrainingSet>>()
    private var trainingSetsOfExerciseFewerRepsParamExerciseID: Int? = null
    private var trainingSetsOfExerciseFewerRepsParamReps: Int? = null

    private val observableTrainingSetsOfExercise = MutableLiveData<List<TrainingSet>>()
    private var trainingSetsOfExerciseParam: Int? = null

    private var lastId = 0

    private fun refreshLiveData() {
        observableTrainingSets.postValue(trainingSets)
        if (trainingSetDatesOfExerciseIsPRParamExerciseID != null) {
            observableTrainingSetDatesOfExerciseIsPR.postValue(
                calcTrainingSetDatesOfExerciseIsPR(
                    trainingSetDatesOfExerciseIsPRParamExerciseID,
                    trainingSetDatesOfExerciseIsPRParamIsPR!!
                )
            )
        }
        if (trainingSetsOfExerciseAndIsPRParamExerciseID != null) {
            observableTrainingSetsOfExerciseAndIsPR.postValue(
                calcTrainingSetsOfExerciseAndIsPR(
                    trainingSetsOfExerciseAndIsPRParamExerciseID,
                    trainingSetsOfExerciseAndIsPRParamIsPR!!
                )
            )
        }
        if (trainingSetsOfExerciseInstanceParam != null) {
            observableTrainingSetsOfExerciseInstance.postValue(
                calcTrainingSetsOfExerciseInstance(
                    trainingSetsOfExerciseInstanceParam
                )
            )
        }
        if (trainingSetsOfExerciseRepsIsPRParamExerciseID != null) {
            observableTrainingSetsOfExerciseRepsIsPR.postValue(
                calcTrainingSetsOfExerciseRepsIsPR(
                    trainingSetsOfExerciseRepsIsPRParamExerciseID,
                    trainingSetsOfExerciseRepsIsPRParamReps!!,
                    trainingSetsOfExerciseRepsIsPRParamIsPR!!
                )
            )
        }
        if (trainingSetsOfExerciseFewerRepsParamExerciseID != null) {
            observableTrainingSetsOfExerciseFewerReps.postValue(
                calcTrainingSetsOfExerciseFewerReps(
                    trainingSetsOfExerciseFewerRepsParamExerciseID,
                    trainingSetsOfExerciseFewerRepsParamReps!!
                )
            )
        }
        if (trainingSetsOfExerciseParam != null) {
            observableTrainingSetsOfExercise.postValue(
                calcTrainingSetsOfExercise(trainingSetsOfExerciseParam)
            )
        }
    }

    override suspend fun insert(trainingSet: TrainingSet) {
        if (trainingSet.trainingSetID == null) {
            trainingSet.trainingSetID = ++lastId
        }
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
        trainingSetDatesOfExerciseIsPRParamExerciseID = exerciseID
        trainingSetDatesOfExerciseIsPRParamIsPR = isPR
        refreshLiveData()
        return observableTrainingSetDatesOfExerciseIsPR
    }

    private fun calcTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int): List<String> {
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

        return dates
    }

    override fun getTrainingSetsOfExerciseAndIsPR(
        exerciseID: Int?,
        isPR: Int
    ): LiveData<List<TrainingSet>> {
        trainingSetsOfExerciseAndIsPRParamExerciseID = exerciseID
        trainingSetsOfExerciseAndIsPRParamIsPR = isPR
        refreshLiveData()
        return observableTrainingSetsOfExerciseAndIsPR
    }

    private fun calcTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int): List<TrainingSet> {
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

        return trainingSetsToReturn.sortedBy { it.reps }
    }

    override fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>> {
        trainingSetsOfExerciseInstanceParam = exerciseInstanceID
        refreshLiveData()
        return observableTrainingSetsOfExerciseInstance
    }

    private fun calcTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): List<TrainingSet> {
        val setsToReturn = arrayListOf<TrainingSet>()
        for (trainingSet in trainingSets) {
            if (trainingSet.exerciseInstanceID == exerciseInstanceID) {
                setsToReturn.add(trainingSet)
            }
        }
        setsToReturn.sortBy { it.trainingSetNumber }
        return setsToReturn
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
        trainingSetsOfExerciseRepsIsPRParamExerciseID = exerciseID
        trainingSetsOfExerciseRepsIsPRParamReps = reps
        trainingSetsOfExerciseRepsIsPRParamIsPR = isPR
        refreshLiveData()
        return observableTrainingSetsOfExerciseRepsIsPR
    }

    private fun calcTrainingSetsOfExerciseRepsIsPR(
        exerciseID: Int?,
        reps: Int,
        isPR: Int
    ): List<TrainingSet> {
        TODO("Joins are going to be a pain")
    }

    override fun getTrainingSetsOfExerciseFewerReps(
        exerciseID: Int?,
        reps: Int
    ): LiveData<List<TrainingSet>> {
        trainingSetsOfExerciseFewerRepsParamExerciseID = exerciseID
        trainingSetsOfExerciseFewerRepsParamReps = reps
        refreshLiveData()
        return observableTrainingSetsOfExerciseFewerReps
    }

    private fun calcTrainingSetsOfExerciseFewerReps(
        exerciseID: Int?,
        reps: Int
    ): List<TrainingSet> {
        TODO("Joins are going to be a pain")
    }

    override fun getTrainingSetsOfExercise(exerciseID: Int?): LiveData<List<TrainingSet>> {
        trainingSetsOfExerciseParam = exerciseID
        refreshLiveData()
        return observableTrainingSetsOfExercise
    }

    private fun calcTrainingSetsOfExercise(exerciseID: Int?): List<TrainingSet> {
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

        return trainingSetsOfExercise
    }
}