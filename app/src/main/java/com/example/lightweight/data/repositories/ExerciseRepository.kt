package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Exercise

class ExerciseRepository(private val db: WorkoutDatabase) : IExerciseRepository {

    override suspend fun insert(exercise: Exercise) = db.getExerciseDao().insert(exercise)

    override suspend fun updateName(exerciseID: Int?, exerciseName: String) = db.getExerciseDao()
        .updateName(exerciseID, exerciseName)

    override suspend fun delete(exercise: Exercise) = db.getExerciseDao().delete(exercise)

    override fun getExerciseOfID(exerciseID: Int?) = db.getExerciseDao().getExerciseOfID(exerciseID)

    override fun getExercisesOfCategory(categoryID: Int?) = db.getExerciseDao()
        .getExercisesOfCategory(categoryID)
}