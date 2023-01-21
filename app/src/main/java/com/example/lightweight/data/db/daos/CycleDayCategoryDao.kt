package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.CycleDayCategory

@Dao
interface CycleDayCategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycleDayCategory: CycleDayCategory)

    @Delete
    suspend fun delete(cycleDayCategory: CycleDayCategory)

    @Query("SELECT * FROM CYCLE_DAY_CATEGORY ORDER BY cycle_day_category_number")
    fun getAllCycleDayCategories(): LiveData<List<CycleDayCategory>>
}