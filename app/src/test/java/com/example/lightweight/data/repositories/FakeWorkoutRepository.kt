package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Workout

class FakeWorkoutRepository : WorkoutRepositoryInterface {

    private val workouts = mutableListOf<Workout>()
    private val observableWorkouts = MutableLiveData<List<Workout>>(workouts)

    private fun refreshLiveData() {
        observableWorkouts.postValue(workouts)
    }

    override suspend fun insert(workout: Workout) {
        workouts.add(workout)
        refreshLiveData()
    }

    override suspend fun delete(workout: Workout) {
        workouts.remove(workout)
        refreshLiveData()
    }

    override suspend fun deleteWorkoutOfID(workoutID: Int?) {
        workouts.removeIf { it.workoutID == workoutID }
        refreshLiveData()
    }

    override fun getWorkoutOfDate(date: String): Workout? {
        for (workout in workouts) {
            if (workout.date == date) {
                return workout
            }
        }

        return null
    }

    override fun getWorkoutDates(): LiveData<List<String>> {
        val workoutDates = mutableListOf<String>()

        for (workout in workouts) {
            workoutDates.add(workout.date)
        }
        workoutDates.sort()

        return MutableLiveData(workoutDates)
    }
}