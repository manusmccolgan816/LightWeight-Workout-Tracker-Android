package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.CycleDayCategoryExerciseCombo
import com.example.lightweight.CycleItem
import com.example.lightweight.data.db.entities.CycleDayExercise

class FakeCycleDayExerciseRepository : CycleDayExerciseRepositoryInterface {

    private val cycleDayExercises = mutableListOf<CycleDayExercise>()
    private val observableCycleDayExercises =
        MutableLiveData<List<CycleDayExercise>>(cycleDayExercises)

    override suspend fun insert(cycleDayExercise: CycleDayExercise) {
        TODO("Not yet implemented")
    }

    override suspend fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?,
        cycleDayExerciseNumber: Int
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(cycleDayExercise: CycleDayExercise) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOfID(cycleDayExerciseID: Int?) {
        TODO("Not yet implemented")
    }

    override fun getCycleItemsOfCycleID(cycleID: Int?): LiveData<List<CycleItem>> {
        TODO("Not yet implemented")
    }

    override fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): LiveData<Int> {
        TODO("Not yet implemented")
    }

    override fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?): List<CycleDayCategoryExerciseCombo> {
        TODO("Not yet implemented")
    }

    override fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?): LiveData<CycleDayExercise> {
        TODO("Not yet implemented")
    }
}