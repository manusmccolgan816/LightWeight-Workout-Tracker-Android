package com.example.lightweight.ui.cycleplanning.cycledayexercise

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.CycleDayExercise
import com.example.lightweight.data.repositories.CycleDayExerciseRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleDayExerciseViewModel(private val repository: CycleDayExerciseRepositoryInterface) : ViewModel() {

    fun insert(cycleDayExercise: CycleDayExercise) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycleDayExercise)
    }

    fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?,
        cycleDayExerciseNumber: Int
    ) = CoroutineScope(Dispatchers.Main).launch {
        repository.decrementCycleDayExerciseNumbersAfter(cycleDayCategoryID, cycleDayExerciseNumber)
    }

    fun delete(cycleDayExercise: CycleDayExercise) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycleDayExercise)
    }

    fun deleteOfID(cycleDayExerciseID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteOfID(cycleDayExerciseID)
    }

    fun getCycleItemsOfCycleID(cycleID: Int?) = repository.getCycleItemsOfCycleID(cycleID)

    fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?) =
        repository.getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID)

    fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?) =
        repository.getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID)

    fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?) =
        repository.getCycleDayExerciseOfID(cycleDayExerciseID)
}