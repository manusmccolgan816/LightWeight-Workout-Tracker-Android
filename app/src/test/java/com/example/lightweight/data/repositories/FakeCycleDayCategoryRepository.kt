package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.CycleDayCategoryCombo
import com.example.lightweight.data.db.entities.CycleDayCategory

class FakeCycleDayCategoryRepository : CycleDayCategoryRepositoryInterface {

    private val cycleDayCategories = mutableListOf<CycleDayCategory>()
    private val observableCycleDayCategories =
        MutableLiveData<List<CycleDayCategory>>(cycleDayCategories)

    override suspend fun insert(cycleDayCategory: CycleDayCategory) {
        TODO("Not yet implemented")
    }

    override suspend fun decrementCycleDayCategoryNumbersAfter(
        cycleDayID: Int?,
        cycleDayCategoryNumber: Int
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(cycleDayCategory: CycleDayCategory) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOfID(cycleDayCategoryID: Int?) {
        TODO("Not yet implemented")
    }

    override fun getCycleDayCatCombosOfCycle(cycleID: Int?): List<CycleDayCategoryCombo> {
        TODO("Not yet implemented")
    }

    override fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?): LiveData<Int?> {
        TODO("Not yet implemented")
    }

    override fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int> {
        TODO("Not yet implemented")
    }

    override fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?): LiveData<CycleDayCategory> {
        TODO("Not yet implemented")
    }
}