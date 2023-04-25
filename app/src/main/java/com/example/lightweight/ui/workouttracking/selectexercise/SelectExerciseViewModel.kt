package com.example.lightweight.ui.workouttracking.selectexercise

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.repositories.ICategoryRepository
import com.example.lightweight.data.repositories.IExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectExerciseViewModel(
    private val categoryRepository: ICategoryRepository,
    private val exerciseRepository: IExerciseRepository
) : ViewModel() {

    fun getCategoryOfID(categoryID: Int?) = categoryRepository.getCategoryOfID(categoryID)


    fun insertExercise(exercise: Exercise) = CoroutineScope(Dispatchers.Main).launch {
        exerciseRepository.insert(exercise)
    }

    fun updateExerciseName(exerciseID: Int?, exerciseName: String) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseRepository.updateName(exerciseID, exerciseName)
        }

    fun deleteExercise(exercise: Exercise) = CoroutineScope(Dispatchers.Main).launch {
        exerciseRepository.delete(exercise)
    }

    fun getExercisesOfCategory(categoryID: Int?) =
        exerciseRepository.getExercisesOfCategory(categoryID)
}