package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDayCategory

class CycleDayCategoryRepository(private val db: WorkoutDatabase) :
    ICycleDayCategoryRepository {

    override suspend fun insert(cycleDayCategory: CycleDayCategory) =
        db.getCycleDayCategoryDao().insert(cycleDayCategory)

    override suspend fun decrementCycleDayCategoryNumbersAfter(
        cycleDayID: Int?,
        cycleDayCategoryNumber: Int
    ) = db.getCycleDayCategoryDao()
        .decrementCycleDayCategoryNumbersAfter(cycleDayID, cycleDayCategoryNumber)

    override suspend fun delete(cycleDayCategory: CycleDayCategory) =
        db.getCycleDayCategoryDao().delete(cycleDayCategory)

    override suspend fun deleteOfID(cycleDayCategoryID: Int?) =
        db.getCycleDayCategoryDao().deleteOfID(cycleDayCategoryID)

    override fun getCycleDayCatCombosOfCycle(cycleID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayCatCombosOfCycle(cycleID)

    override fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?) =
        db.getCycleDayCategoryDao().getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID)

    override fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        db.getCycleDayCategoryDao().getNumCycleDayCategoriesOfCycleDay(cycleDayID)

    override fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayCategoryOfID(cycleDayCategoryID)
}