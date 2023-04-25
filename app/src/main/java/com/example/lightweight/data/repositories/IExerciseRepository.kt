package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.data.db.entities.Exercise

interface IExerciseRepository {
    suspend fun insert(exercise: Exercise)
    suspend fun updateName(exerciseID: Int?, exerciseName: String)
    suspend fun delete(exercise: Exercise)
    fun getExerciseOfID(exerciseID: Int?): Exercise
    fun getExercisesOfCategory(categoryID: Int?): LiveData<List<Exercise>>
}