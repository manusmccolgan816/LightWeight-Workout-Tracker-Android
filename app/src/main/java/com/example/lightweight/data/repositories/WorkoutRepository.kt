package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Category

class WorkoutRepository(
    private val db: WorkoutDatabase
    ) {

    suspend fun upsert(category: Category) = db.getCategoryDao().upsert(category)

    suspend fun delete(category: Category) = db.getCategoryDao().delete(category)

    fun getAllCategories() = db.getCategoryDao().getAllCategories()
}