package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryCombo
import com.example.lightweight.data.db.entities.CycleDayCategory

class FakeCycleDayCategoryRepository : CycleDayCategoryRepositoryInterface {

    private val cycleDayCategories = mutableListOf<CycleDayCategory>()
    private val observableCycleDayCategories =
        MutableLiveData<List<CycleDayCategory>>(cycleDayCategories)

    private fun refreshLiveData() {
        observableCycleDayCategories.postValue(cycleDayCategories)
    }

    override suspend fun insert(cycleDayCategory: CycleDayCategory) {
        cycleDayCategories.add(cycleDayCategory)
        refreshLiveData()
    }

    override suspend fun decrementCycleDayCategoryNumbersAfter(
        cycleDayID: Int?,
        cycleDayCategoryNumber: Int
    ) {
        for (cycleDayCategory in cycleDayCategories.filter {
            it.cycleDayID == cycleDayID && it.cycleDayCategoryNumber > cycleDayCategoryNumber
        }) {
            cycleDayCategory.cycleDayCategoryNumber--
            refreshLiveData()
        }
    }

    override suspend fun delete(cycleDayCategory: CycleDayCategory) {
        cycleDayCategories.remove(cycleDayCategory)
        refreshLiveData()
    }

    override suspend fun deleteOfID(cycleDayCategoryID: Int?) {
        cycleDayCategories.removeIf { it.cycleDayCategoryID == cycleDayCategoryID }
        refreshLiveData()
    }

    override fun getCycleDayCatCombosOfCycle(cycleID: Int?): List<CycleDayCategoryCombo> {
        TODO("Joins are going to be a pain")
    }

    override fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?): LiveData<Int?> {
        return MutableLiveData(
            cycleDayCategories.filter { it.cycleDayCategoryID == cycleDayCategoryID }[0].categoryID
        )
    }

    override fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int> {
        return MutableLiveData(
            cycleDayCategories.filter { it.cycleDayID == cycleDayID }.size
        )
    }

    override fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?): LiveData<CycleDayCategory> {
        return MutableLiveData(
            cycleDayCategories.filter { it.cycleDayCategoryID == cycleDayCategoryID }[0]
        )
    }
}