package com.example.lightweight.ui.exerciseinstance

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.repositories.ExerciseInstanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseInstanceViewModel(private val repository: ExerciseInstanceRepository) : ViewModel() {

    fun insert(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(exerciseInstance)
    }

    fun delete(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(exerciseInstance)
    }

    // Doesn't need to be executed in a coroutine because it is just a read operation
    fun getAllExerciseInstances() = repository.getAllExerciseInstances()

    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?) =
        repository.getExerciseInstance(workoutID, exerciseID)
}