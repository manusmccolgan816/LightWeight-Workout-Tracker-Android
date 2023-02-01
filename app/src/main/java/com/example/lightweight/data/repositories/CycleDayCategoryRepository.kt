package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDayCategory

class CycleDayCategoryRepository(private val db: WorkoutDatabase) {

    suspend fun insert(cycleDayCategory: CycleDayCategory) =
        db.getCycleDayCategoryDao().insert(cycleDayCategory)

    suspend fun delete(cycleDayCategory: CycleDayCategory) =
        db.getCycleDayCategoryDao().delete(cycleDayCategory)

    suspend fun deleteOfID(cycleDayCategoryID: Int?) =
        db.getCycleDayCategoryDao().deleteOfID(cycleDayCategoryID)

    fun getAllCycleDayCategories() = db.getCycleDayCategoryDao().getAllCycleDayCategories()

    fun getCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayCategoriesOfCycleDay(cycleDayID)

    fun getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID)

    fun getCycleDayCategoriesNamesCycleDaysOfCycleDay(cycleDayID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayCategoriesNamesCycleDaysOfCycleDay(cycleDayID)

    fun getCycleDayAndCycleDayCategoriesOfCycle(cycleID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayAndCycleDayCategoriesOfCycle(cycleID)

    fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?) =
        db.getCycleDayCategoryDao().getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID)

    fun getCycleDayIDOfCycleDayCategoryID(cycleDayCategoryID: Int?) =
        db.getCycleDayCategoryDao().getCycleDayIDOfCycleDayCategoryID(cycleDayCategoryID)

    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        db.getCycleDayCategoryDao().getNumCycleDayCategoriesOfCycleDay(cycleDayID)
}