package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Workout

class FakeWorkoutRepository : WorkoutRepositoryInterface {

    private val allTag = 0
    private val workoutsTag = 1
    private val workoutDatesTag = 2

    var exerciseInstanceRepo: FakeExerciseInstanceRepository? = null

    private val workouts = mutableListOf<Workout>()
    val observableWorkouts = MutableLiveData<List<Workout>>(workouts)

    private val observableWorkoutDates = MutableLiveData<List<String>>()

    private var lastId = 0

    private fun refreshLiveData(tag: Int) {
        if (tag == workoutsTag || tag == allTag) observableWorkouts.postValue(workouts)
        if (tag == workoutDatesTag || tag == allTag) {
            observableWorkoutDates.postValue(calcWorkoutDates())
        }
    }

    override suspend fun insert(workout: Workout) {
        if (workout.workoutID == null) {
            workout.workoutID = ++lastId
        }
        workouts.add(workout)
        refreshLiveData(allTag)

        if (exerciseInstanceRepo != null) {
            exerciseInstanceRepo?.workouts?.add(workout)
            exerciseInstanceRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun delete(workout: Workout) {
        workouts.remove(workout)
        refreshLiveData(allTag)

        if (exerciseInstanceRepo != null) {
            exerciseInstanceRepo?.workouts?.remove(workout)
            exerciseInstanceRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun deleteWorkoutOfID(workoutID: Int?) {
        workouts.removeIf { it.workoutID == workoutID }
        refreshLiveData(allTag)

        if (exerciseInstanceRepo != null) {
            exerciseInstanceRepo?.workouts?.removeIf { it.workoutID == workoutID }
            exerciseInstanceRepo?.refreshLiveData(allTag)
        }
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
        refreshLiveData(workoutDatesTag)
        return observableWorkoutDates
    }

    private fun calcWorkoutDates(): List<String> {
        val workoutDates = mutableListOf<String>()

        for (workout in workouts) {
            workoutDates.add(workout.date)
        }
        workoutDates.sort()

        return workoutDates
    }
}