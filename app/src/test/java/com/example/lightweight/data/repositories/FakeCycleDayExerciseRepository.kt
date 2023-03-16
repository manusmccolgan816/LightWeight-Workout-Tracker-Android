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

    private fun refreshLiveData() {
        observableCycleDayExercises.postValue(cycleDayExercises)
    }

    override suspend fun insert(cycleDayExercise: CycleDayExercise) {
        cycleDayExercises.add(cycleDayExercise)
        refreshLiveData()
    }

    override suspend fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?,
        cycleDayExerciseNumber: Int
    ) {
        for (cycleDayExercise in cycleDayExercises.filter {
            it.cycleDayCategoryID == cycleDayCategoryID
                    && it.cycleDayExerciseNumber > cycleDayExerciseNumber
        }) {
            cycleDayExercise.cycleDayExerciseNumber--
            refreshLiveData()
        }
    }

    override suspend fun delete(cycleDayExercise: CycleDayExercise) {
        cycleDayExercises.remove(cycleDayExercise)
        refreshLiveData()
    }

    override suspend fun deleteOfID(cycleDayExerciseID: Int?) {
        cycleDayExercises.removeIf { it.cycleDayExerciseID == cycleDayExerciseID }
        refreshLiveData()
    }

    override fun getCycleItemsOfCycleID(cycleID: Int?): LiveData<List<CycleItem>> {
        TODO("Joins are going to be a pain")
    }

    override fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): LiveData<Int> {
        return MutableLiveData(
            cycleDayExercises.filter { it.cycleDayCategoryID == cycleDayCategoryID }.size
        )
    }

    override fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?):
            List<CycleDayCategoryExerciseCombo> {
        TODO("Joins are going to be a pain")
    }

    override fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?): LiveData<CycleDayExercise> {
        return MutableLiveData(
            cycleDayExercises.filter { it.cycleDayExerciseID == cycleDayExerciseID }[0]
        )
    }
}