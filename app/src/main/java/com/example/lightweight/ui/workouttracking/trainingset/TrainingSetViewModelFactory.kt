package com.example.lightweight.ui.workouttracking.trainingset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.TrainingSetRepositoryInterface

class TrainingSetViewModelFactory(
    private val repository: TrainingSetRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrainingSetViewModel(repository) as T
    }
}