package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Workout

class WorkoutRepository(private val db: WorkoutDatabase) {

    suspend fun insert(workout: Workout) = db.getWorkoutDao().insert(workout)

    suspend fun delete(workout: Workout) = db.getWorkoutDao().delete(workout)

    fun getAllWorkouts() = db.getWorkoutDao().getAllWorkouts()
}