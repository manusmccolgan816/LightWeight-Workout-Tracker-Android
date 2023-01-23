package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Cycle

@Dao
interface CycleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycle: Cycle)

    @Update
    suspend fun update(cycle: Cycle)

    @Delete
    suspend fun delete(cycle: Cycle)

    @Query("SELECT * FROM CYCLE ORDER BY cycle_name")
    fun getAllCycles(): LiveData<List<Cycle>>
}