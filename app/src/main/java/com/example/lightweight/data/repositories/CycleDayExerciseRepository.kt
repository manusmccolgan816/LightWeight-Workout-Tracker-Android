package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDayExercise

class CycleDayExerciseRepository(private val db: WorkoutDatabase) {

    suspend fun insert(cycleDayExercise: CycleDayExercise) =
        db.getCycleDayExerciseDao().insert(cycleDayExercise)

    suspend fun delete(cycleDayExercise: CycleDayExercise) =
        db.getCycleDayExerciseDao().delete(cycleDayExercise)

    fun getAllCycleDayExercises() = db.getCycleDayExerciseDao().getAllCycleDayExercises()
}