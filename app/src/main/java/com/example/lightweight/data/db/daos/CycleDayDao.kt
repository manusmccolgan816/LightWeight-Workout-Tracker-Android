package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.CycleDay

@Dao
interface CycleDayDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycleDay: CycleDay)

    @Delete
    suspend fun delete(cycleDay: CycleDay)

    @Query("SELECT * FROM CYCLE_DAY ORDER BY cycle_day_number")
    fun getAllCycleDays(): LiveData<List<CycleDay>>
}