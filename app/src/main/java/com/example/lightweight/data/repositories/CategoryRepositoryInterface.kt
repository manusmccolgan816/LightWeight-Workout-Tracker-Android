package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.data.db.entities.Category

interface CategoryRepositoryInterface {
    suspend fun insert(category: Category)
    suspend fun update(categoryID: Int?, newName: String)
    suspend fun delete(category: Category)
    fun getAllCategories(): LiveData<List<Category>>
    fun getCategoryOfID(categoryID: Int?): Category
    fun getCategoryOfIDObs(categoryID: Int?): LiveData<Category>
}