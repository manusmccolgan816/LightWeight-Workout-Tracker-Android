package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Exercise

class ExerciseRepository(private val db: WorkoutDatabase) {

    suspend fun insert(exercise: Exercise) = db.getExerciseDao().insert(exercise)

    suspend fun updateName(exerciseID: Int?, exerciseName: String) = db.getExerciseDao()
        .updateName(exerciseID, exerciseName)

    suspend fun delete(exercise: Exercise) = db.getExerciseDao().delete(exercise)

    fun getAllExercises() = db.getExerciseDao().getAllExercises()

    fun getExerciseOfID(exerciseID: Int?) = db.getExerciseDao().getExerciseOfID(exerciseID)

    fun getExercisesOfCategory(categoryID: Int?) = db.getExerciseDao()
        .getExercisesOfCategory(categoryID)
}