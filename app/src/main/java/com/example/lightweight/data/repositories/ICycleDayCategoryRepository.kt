package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryCombo
import com.example.lightweight.data.db.entities.CycleDayCategory

interface ICycleDayCategoryRepository {
    suspend fun insert(cycleDayCategory: CycleDayCategory)
    suspend fun decrementCycleDayCategoryNumbersAfter(
        cycleDayID: Int?,
        cycleDayCategoryNumber: Int
    )
    suspend fun delete(cycleDayCategory: CycleDayCategory)
    suspend fun deleteOfID(cycleDayCategoryID: Int?)
    fun getCycleDayCatCombosOfCycle(cycleID: Int?): List<CycleDayCategoryCombo>
    fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?): LiveData<Int?>
    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int>
    fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?): LiveData<CycleDayCategory>
}