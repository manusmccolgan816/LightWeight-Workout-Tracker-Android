package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.CycleDayCategoryCombo
import com.example.lightweight.data.db.entities.CycleDayCategory

@Dao
interface CycleDayCategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycleDayCategory: CycleDayCategory)

    @Query("UPDATE CYCLE_DAY_CATEGORY " +
            "SET cycle_day_category_number = cycle_day_category_number - 1 " +
            "WHERE cycle_day_ID = :cycleDayID " +
            "AND cycle_day_category_number > :cycleDayCategoryNumber")
    suspend fun decrementCycleDayCategoryNumbersAfter(cycleDayID: Int?, cycleDayCategoryNumber: Int)

    @Delete
    suspend fun delete(cycleDayCategory: CycleDayCategory)

    @Query("DELETE FROM CYCLE_DAY_CATEGORY WHERE cycle_day_category_ID = :cycleDayCategoryID")
    suspend fun deleteOfID(cycleDayCategoryID: Int?)

    @Query("SELECT * FROM CYCLE_DAY_CATEGORY ORDER BY cycle_day_category_number")
    fun getAllCycleDayCategories(): LiveData<List<CycleDayCategory>>

    @Query("SELECT CDC.cycle_day_category_ID, C.category_name, CD.cycle_day_ID " +
            "FROM CYCLE_DAY AS CD " +
            "INNER JOIN CYCLE_DAY_CATEGORY AS CDC " +
            "ON CD.cycle_day_ID = CDC.cycle_day_ID " +
            "INNER JOIN CATEGORY AS C " +
            "ON CDC.category_ID = C.category_ID " +
            "WHERE CD.cycle_day_ID = :cycleDayID " +
            "ORDER BY cycle_day_category_number")
    fun getCycleDayCatCombosOfCycle(cycleDayID: Int?): List<CycleDayCategoryCombo>

    @Query("SELECT category_ID " +
            "FROM CYCLE_DAY_CATEGORY " +
            "WHERE cycle_day_category_ID = :cycleDayCategoryID")
    fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?): LiveData<Int?>

    @Query("SELECT COUNT(cycle_day_category_number) " +
            "FROM CYCLE_DAY_CATEGORY " +
            "WHERE cycle_day_ID = :cycleDayID")
    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int>

    @Query("SELECT * " +
            "FROM CYCLE_DAY_CATEGORY " +
            "WHERE cycle_day_category_ID = :cycleDayCategoryID")
    fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?): LiveData<CycleDayCategory>
}