package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.CycleDayCategoryCombo
import com.example.lightweight.CycleDayCycleDayCatCombo
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

    @Query("SELECT CDC.cycle_day_category_ID, C.category_name, CDC.cycle_day_ID " +
            "FROM CYCLE_DAY_CATEGORY AS CDC " +
            "INNER JOIN CATEGORY AS C " +
            "ON CDC.category_ID = C.category_ID " +
            "WHERE cycle_day_ID = :cycleDayID " +
            "ORDER BY cycle_day_category_number")
    fun getCycleDayCategoriesNamesCycleDaysOfCycleDay(cycleDayID: Int?): LiveData<List<CycleDayCategoryCombo>>

    @Query("SELECT CDC.cycle_day_category_ID, CDC.cycle_day_category_number, C.category_name, CD.cycle_day_ID, CD.cycle_day_name, CD.cycle_day_number " +
            "FROM CYCLE_DAY AS CD " +
            "LEFT JOIN CYCLE_DAY_CATEGORY AS CDC " +
            "ON CD.cycle_day_ID = CDC.cycle_day_ID " +
            "LEFT JOIN CATEGORY AS C " +
            "ON CDC.category_ID = C.category_ID " +
            "WHERE cycle_ID = :cycleID " +
            "ORDER BY cycle_day_number, cycle_day_category_number")
    fun getCycleDayAndCycleDayCategoriesOfCycle(cycleID: Int?): LiveData<List<CycleDayCycleDayCatCombo>>

    @Query("SELECT COUNT(cycle_day_category_number) FROM CYCLE_DAY_CATEGORY WHERE cycle_day_ID = :cycleDayID")
    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int>
}