package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.data.db.entities.Workout

interface WorkoutRepositoryInterface {
    suspend fun insert(workout: Workout)
    suspend fun delete(workout: Workout)
    suspend fun deleteWorkoutOfID(workoutID: Int?)
    fun getWorkoutOfDate(date: String): Workout?
    fun getWorkoutDates(): LiveData<List<String>>
}