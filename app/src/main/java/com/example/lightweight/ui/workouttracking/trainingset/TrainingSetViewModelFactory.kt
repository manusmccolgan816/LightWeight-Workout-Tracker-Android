package com.example.lightweight.ui.workouttracking.trainingset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ITrainingSetRepository

class TrainingSetViewModelFactory(
    private val repository: ITrainingSetRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrainingSetViewModel(repository) as T
    }
}