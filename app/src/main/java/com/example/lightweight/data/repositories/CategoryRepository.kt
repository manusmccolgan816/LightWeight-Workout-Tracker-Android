package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Category

class CategoryRepository(private val db: WorkoutDatabase) : ICategoryRepository {

    override suspend fun insert(category: Category) = db.getCategoryDao().insert(category)

    override suspend fun update(categoryID: Int?, newName: String) =
        db.getCategoryDao().update(categoryID, newName)

    override suspend fun delete(category: Category) = db.getCategoryDao().delete(category)

    override fun getAllCategories() = db.getCategoryDao().getAllCategories()

    override fun getCategoryOfID(categoryID: Int?) = db.getCategoryDao().getCategoryOfID(categoryID)

    override fun getCategoryOfIDObs(categoryID: Int?) = db.getCategoryDao().getCategoryOfIDObs(categoryID)
}