package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.TrainingSet

class TrainingSetRepository(private val db: WorkoutDatabase) {

    suspend fun insert(trainingSet: TrainingSet) = db.getTrainingSetDao().insert(trainingSet)

    suspend fun delete(trainingSet: TrainingSet) = db.getTrainingSetDao().delete(trainingSet)

    suspend fun setIsPRFalse(trainingSetID: Int?) = db.getTrainingSetDao().setIsPRFalse(trainingSetID)

    fun getAllTrainingSets() = db.getTrainingSetDao().getAllTrainingSets()

    fun getPRTrainingSetsOfExercise(exerciseID: Int?) =
        db.getTrainingSetDao().getPRTrainingSetsOfExercise(exerciseID)

    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseInstance(exerciseInstanceID)
}