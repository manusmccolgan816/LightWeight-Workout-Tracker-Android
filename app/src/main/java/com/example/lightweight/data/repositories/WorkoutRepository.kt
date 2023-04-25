package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Workout

class WorkoutRepository(private val db: WorkoutDatabase) : IWorkoutRepository {

    override suspend fun insert(workout: Workout) = db.getWorkoutDao().insert(workout)

    override suspend fun delete(workout: Workout) = db.getWorkoutDao().delete(workout)

    override suspend fun deleteWorkoutOfID(workoutID: Int?) =
        db.getWorkoutDao().deleteWorkoutOfID(workoutID)

    override fun getWorkoutOfDate(date: String) = db.getWorkoutDao().getWorkoutOfDate(date)

    override fun getWorkoutDates() = db.getWorkoutDao().getWorkoutDates()
}