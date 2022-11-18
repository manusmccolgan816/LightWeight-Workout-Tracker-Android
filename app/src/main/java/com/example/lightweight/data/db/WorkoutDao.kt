package com.example.lightweight.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Category

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM CATEGORY")
    fun getAllCategories(): LiveData<List<Category>>
}