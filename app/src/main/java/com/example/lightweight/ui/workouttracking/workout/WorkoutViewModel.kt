package com.example.lightweight.ui.workouttracking.workout

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.data.repositories.IWorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: IWorkoutRepository) : ViewModel() {

    fun insert(workout: Workout) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(workout)
    }

    fun delete(workout: Workout) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(workout)
    }

    fun deleteWorkoutOfID(workoutID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteWorkoutOfID(workoutID)
    }

    fun getWorkoutOfDate(date: String) = repository.getWorkoutOfDate(date)

    fun getWorkoutDates() = repository.getWorkoutDates()
}