package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Workout

class FakeWorkoutRepository : WorkoutRepositoryInterface {

    private val workouts = mutableListOf<Workout>()
    private val observableWorkouts = MutableLiveData<List<Workout>>(workouts)

    override suspend fun insert(workout: Workout) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(workout: Workout) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWorkoutOfID(workoutID: Int?) {
        TODO("Not yet implemented")
    }

    override fun getWorkoutOfDate(date: String): Workout? {
        TODO("Not yet implemented")
    }

    override fun getWorkoutDates(): LiveData<List<String>> {
        TODO("Not yet implemented")
    }
}