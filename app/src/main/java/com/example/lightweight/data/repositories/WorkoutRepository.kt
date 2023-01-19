package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Workout

class WorkoutRepository(private val db: WorkoutDatabase) {

    suspend fun insert(workout: Workout) = db.getWorkoutDao().insert(workout)

    suspend fun delete(workout: Workout) = db.getWorkoutDao().delete(workout)

    suspend fun deleteWorkoutOfID(workoutID: Int?) = db.getWorkoutDao().deleteWorkoutOfID(workoutID)

    fun getAllWorkouts() = db.getWorkoutDao().getAllWorkouts()

    fun getWorkoutOfDate(date: String) = db.getWorkoutDao().getWorkoutOfDate(date)

    fun getWorkoutDates() = db.getWorkoutDao().getWorkoutDates()
}