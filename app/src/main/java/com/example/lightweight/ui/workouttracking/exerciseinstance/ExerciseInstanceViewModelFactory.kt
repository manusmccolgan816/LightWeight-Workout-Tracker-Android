package com.example.lightweight.ui.workouttracking.exerciseinstance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.IExerciseInstanceRepository

class ExerciseInstanceViewModelFactory(
    private val repository: IExerciseInstanceRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExerciseInstanceViewModel(repository) as T
    }
}