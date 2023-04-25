package com.example.lightweight.ui.workouttracking.selectexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ICategoryRepository
import com.example.lightweight.data.repositories.IExerciseRepository

class SelectExerciseViewModelFactory(
    private val categoryRepository: ICategoryRepository,
    private val exerciseRepository: IExerciseRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectExerciseViewModel(
            categoryRepository,
            exerciseRepository
        ) as T
    }
}