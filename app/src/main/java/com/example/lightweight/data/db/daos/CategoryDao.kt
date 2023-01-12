package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    @Query("UPDATE CATEGORY SET category_name = :categoryName WHERE category_ID = :categoryID")
    suspend fun update(categoryID: Int?, categoryName: String)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM CATEGORY ORDER BY category_name")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM CATEGORY WHERE category_ID = :categoryID")
    fun getCategoryOfID(categoryID: Int?): Category
}