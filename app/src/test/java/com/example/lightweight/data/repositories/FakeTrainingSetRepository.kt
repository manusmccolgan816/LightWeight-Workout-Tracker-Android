package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.TrainingSet

class FakeTrainingSetRepository : TrainingSetRepositoryInterface {

    private val trainingSets = mutableListOf<TrainingSet>()
    private val observableTrainingSets = MutableLiveData<List<TrainingSet>>(trainingSets)

    override suspend fun insert(trainingSet: TrainingSet) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(trainingSet: TrainingSet) {
        TODO("Not yet implemented")
    }

    override suspend fun update(trainingSet: TrainingSet) {
        TODO("Not yet implemented")
    }

    override suspend fun updateIsPR(trainingSetID: Int?, isPR: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(trainingSetID: Int?, note: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun decrementTrainingSetNumbersAbove(
        exerciseInstanceID: Int?,
        trainingSetNumber: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getAllTrainingSets(): LiveData<List<TrainingSet>> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetDatesOfExerciseIsPR(
        exerciseID: Int?,
        isPR: Int
    ): LiveData<List<String>> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetsOfExerciseAndIsPR(
        exerciseID: Int?,
        isPR: Int
    ): LiveData<List<TrainingSet>> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?): List<TrainingSet> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetsOfExerciseRepsIsPR(
        exerciseID: Int?,
        reps: Int,
        isPR: Int
    ): LiveData<List<TrainingSet>> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetsOfExerciseFewerReps(
        exerciseID: Int?,
        reps: Int
    ): LiveData<List<TrainingSet>> {
        TODO("Not yet implemented")
    }

    override fun getTrainingSetsOfExercise(exerciseID: Int?): LiveData<List<TrainingSet>> {
        TODO("Not yet implemented")
    }
}