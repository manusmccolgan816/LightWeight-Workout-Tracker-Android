package com.example.lightweight.ui.workouttracking.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.WorkoutRepositoryInterface

class WorkoutViewModelFactory(
    private val repository: WorkoutRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WorkoutViewModel(repository) as T
    }
}