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

    @Query("SELECT * " +
            "FROM CYCLE_DAY_CATEGORY " +
            "WHERE cycle_day_ID = :cycleDayID " +
            "ORDER BY cycle_day_category_number")
    fun getCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<List<CycleDayCategory>>

    @MapInfo(keyColumn = "cycle_day_category_ID", valueColumn = "category_name")
    @Query("SELECT CDC.cycle_day_category_ID, C.category_name " +
            "FROM CYCLE_DAY_CATEGORY AS CDC " +
            "INNER JOIN CATEGORY AS C " +
            "ON CDC.category_ID = C.category_ID " +
            "WHERE cycle_day_ID = :cycleDayID " +
            "ORDER BY cycle_day_category_number")
    fun getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID: Int?): LiveData<Map<Int?, String>>

    @Query("SELECT COUNT(cycle_day_number) FROM CYCLE_DAY WHERE cycle_day_ID = :cycleDayID")
    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int>
}