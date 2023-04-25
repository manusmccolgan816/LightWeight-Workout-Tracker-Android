package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.IExerciseInstanceRepository
import com.example.lightweight.data.repositories.IExerciseRepository
import com.example.lightweight.data.repositories.ITrainingSetRepository
import com.example.lightweight.data.repositories.IWorkoutRepository

class LogSetsViewModelFactory(
    private val exerciseRepository: IExerciseRepository,
    private val workoutRepository: IWorkoutRepository,
    private val exerciseInstanceRepository: IExerciseInstanceRepository,
    private val trainingSetRepository: ITrainingSetRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogSetsViewModel(
            exerciseRepository,
            workoutRepository,
            exerciseInstanceRepository,
            trainingSetRepository
        ) as T
    }
}