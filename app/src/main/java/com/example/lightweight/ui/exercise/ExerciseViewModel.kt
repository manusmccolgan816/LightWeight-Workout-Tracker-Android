package com.example.lightweight.ui.exercise

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.repositories.ExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    fun insert(exercise: Exercise) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(exercise)
    }

    fun delete(exercise: Exercise) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(exercise)
    }

    // Doesn't need to be executed in a coroutine because it is just a read operation
    fun getAllExercises() = repository.getAllExercises()
}