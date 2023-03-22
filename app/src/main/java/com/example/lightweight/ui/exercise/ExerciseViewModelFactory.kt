package com.example.lightweight.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ExerciseRepositoryInterface

class ExerciseViewModelFactory(
    private val repository: ExerciseRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExerciseViewModel(repository) as T
    }
}