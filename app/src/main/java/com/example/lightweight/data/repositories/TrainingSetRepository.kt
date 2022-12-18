package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.TrainingSet

class TrainingSetRepository(private val db: WorkoutDatabase) {

    suspend fun insert(trainingSet: TrainingSet) = db.getTrainingSetDao().insert(trainingSet)

    suspend fun delete(trainingSet: TrainingSet) = db.getTrainingSetDao().delete(trainingSet)

    fun getAllTrainingSets() = db.getTrainingSetDao().getAllTrainingSets()
}