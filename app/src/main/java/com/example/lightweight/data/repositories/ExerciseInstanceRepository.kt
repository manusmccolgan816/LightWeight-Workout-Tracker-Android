package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.ExerciseInstance

class ExerciseInstanceRepository(private val db: WorkoutDatabase) {

    suspend fun insert(exerciseInstance: ExerciseInstance) = db.getExerciseInstanceDao().insert(exerciseInstance)

    suspend fun delete(exerciseInstance: ExerciseInstance) = db.getExerciseInstanceDao().delete(exerciseInstance)

    fun getAllExerciseInstances() = db.getExerciseInstanceDao().getAllExerciseInstances()
}