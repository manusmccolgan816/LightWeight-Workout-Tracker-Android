package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Category

class CategoryRepository(private val db: WorkoutDatabase) {

    suspend fun insert(category: Category) = db.getCategoryDao().insert(category)

    suspend fun update(categoryID: Int?, newName: String) = db.getCategoryDao().update(categoryID, newName)

    suspend fun delete(category: Category) = db.getCategoryDao().delete(category)

    fun getAllCategories() = db.getCategoryDao().getAllCategories()

    fun getCategoryOfID(categoryID: Int?) = db.getCategoryDao().getCategoryOfID(categoryID)
}