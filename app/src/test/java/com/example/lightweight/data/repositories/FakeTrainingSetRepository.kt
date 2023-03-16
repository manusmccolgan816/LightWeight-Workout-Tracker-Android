package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.TrainingSet

class FakeTrainingSetRepository : TrainingSetRepositoryInterface {

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
        TODO("Joins are going to be a pain")
    }

    override fun getTrainingSetsOfExerciseAndIsPR(
        exerciseID: Int?,
        isPR: Int
    ): LiveData<List<TrainingSet>> {
        TODO("Joins are going to be a pain")
    }

    override fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>> {
        return MutableLiveData(trainingSets.filter {
            it.exerciseInstanceID == exerciseInstanceID
        })
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
        TODO("Joins are going to be a pain")
    }
}