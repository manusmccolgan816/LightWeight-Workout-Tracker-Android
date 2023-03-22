package com.example.lightweight.ui.workouttracking.exerciseinstance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ExerciseInstanceRepositoryInterface

class ExerciseInstanceViewModelFactory(
    private val repository: ExerciseInstanceRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExerciseInstanceViewModel(repository) as T
    }
}