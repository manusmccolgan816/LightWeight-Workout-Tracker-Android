package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryExerciseCombo
import com.example.lightweight.ui.cycleplanning.CycleItem
import com.example.lightweight.data.db.entities.CycleDayExercise

interface ICycleDayExerciseRepository {
    suspend fun insert(cycleDayExercise: CycleDayExercise)
    suspend fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?, cycleDayExerciseNumber: Int
    )
    suspend fun delete(cycleDayExercise: CycleDayExercise)
    suspend fun deleteOfID(cycleDayExerciseID: Int?)
    fun getCycleItemsOfCycleID(cycleID: Int?): LiveData<List<CycleItem>>
    fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): LiveData<Int>
    fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?): List<CycleDayCategoryExerciseCombo>
    fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?): LiveData<CycleDayExercise>
}