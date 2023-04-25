package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.data.db.entities.TrainingSet

interface ITrainingSetRepository {
    suspend fun insert(trainingSet: TrainingSet)
    suspend fun delete(trainingSet: TrainingSet)
    suspend fun update(trainingSet: TrainingSet)
    suspend fun updateIsPR(trainingSetID: Int?, isPR: Int)
    suspend fun updateNote(trainingSetID: Int?, note: String?)
    suspend fun decrementTrainingSetNumbersAbove(exerciseInstanceID: Int?, trainingSetNumber: Int)
    fun getAllTrainingSets(): LiveData<List<TrainingSet>>
    fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int): LiveData<List<String>>
    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int): LiveData<List<TrainingSet>>
    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>>
    fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?): List<TrainingSet>
    fun getTrainingSetsOfExerciseRepsIsPR(
        exerciseID: Int?,
        reps: Int,
        isPR: Int
    ): LiveData<List<TrainingSet>>
    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int): LiveData<List<TrainingSet>>
    fun getTrainingSetsOfExercise(exerciseID: Int?): LiveData<List<TrainingSet>>
}