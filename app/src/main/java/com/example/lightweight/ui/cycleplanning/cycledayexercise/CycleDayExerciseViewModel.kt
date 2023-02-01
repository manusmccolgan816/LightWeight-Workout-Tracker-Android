package com.example.lightweight.ui.cycleplanning.cycledayexercise

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.CycleDayExercise
import com.example.lightweight.data.repositories.CycleDayExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleDayExerciseViewModel(private val repository: CycleDayExerciseRepository) : ViewModel() {

    fun insert(cycleDayExercise: CycleDayExercise) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycleDayExercise)
    }

    fun delete(cycleDayExercise: CycleDayExercise) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycleDayExercise)
    }

    fun deleteOfID(cycleDayExerciseID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteOfID(cycleDayExerciseID)
    }

    fun getAllCycleDayExercises() = repository.getAllCycleDayExercises()

    fun getCycleDataOfCycleID(cycleID: Int?) = repository.getCycleDataOfCycleID(cycleID)

    fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?) =
        repository.getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID)
}