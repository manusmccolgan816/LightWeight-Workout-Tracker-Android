package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.CycleDay

@Dao
interface CycleDayDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycleDay: CycleDay)

    @Update
    suspend fun update(cycleDay: CycleDay)

    @Query("UPDATE CYCLE_DAY " +
            "SET cycle_day_number = cycle_day_number - 1 " +
            "WHERE cycle_ID = :cycleID " +
            "AND cycle_day_number > :cycleDayNumber")
    suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int)

    @Delete
    suspend fun delete(cycleDay: CycleDay)

    @Query("SELECT * FROM CYCLE_DAY ORDER BY cycle_day_number")
    fun getAllCycleDays(): LiveData<List<CycleDay>>

    @Query("SELECT * FROM CYCLE_DAY WHERE cycle_ID = :cycleID ORDER BY cycle_day_number")
    fun getCycleDaysOfCycle(cycleID: Int?): LiveData<List<CycleDay>>

    @Query("SELECT * FROM CYCLE_DAY WHERE cycle_day_ID = :cycleDayID")
    fun getCycleDayOfID(cycleDayID: Int?): LiveData<CycleDay>
}