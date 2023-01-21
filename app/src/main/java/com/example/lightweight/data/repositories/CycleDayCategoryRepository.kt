package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDayCategory

class CycleDayCategoryRepository(private val db: WorkoutDatabase) {

    suspend fun insert(cycleDayCategory: CycleDayCategory) =
        db.getCycleDayCategoryDao().insert(cycleDayCategory)

    suspend fun delete(cycleDayCategory: CycleDayCategory) =
        db.getCycleDayCategoryDao().delete(cycleDayCategory)

    fun getAllCycleDayCategories() = db.getCycleDayCategoryDao().getAllCycleDayCategories()
}