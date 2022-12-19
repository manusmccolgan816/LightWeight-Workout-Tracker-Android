package com.example.lightweight.ui.workout

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.data.repositories.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {

    fun insert(workout: Workout) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(workout)
    }

    fun delete(workout: Workout) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(workout)
    }

    // Doesn't need to be executed in a coroutine because it is just a read operation
    fun getAllWorkouts() = repository.getAllWorkouts()

    fun getWorkoutOfDate(date: String) = repository.getWorkoutOfDate(date)
}