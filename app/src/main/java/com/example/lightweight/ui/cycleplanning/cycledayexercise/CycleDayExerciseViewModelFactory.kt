package com.example.lightweight.ui.cycleplanning.cycledayexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.CycleDayExerciseRepositoryInterface

class CycleDayExerciseViewModelFactory(
    private val repository: CycleDayExerciseRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CycleDayExerciseViewModel(repository) as T
    }
}