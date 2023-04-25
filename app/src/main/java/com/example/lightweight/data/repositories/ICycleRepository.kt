package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.data.db.entities.Cycle

interface ICycleRepository {
    suspend fun insert(cycle: Cycle)
    suspend fun update(cycle: Cycle)
    suspend fun delete(cycle: Cycle)
    fun getAllCycles(): LiveData<List<Cycle>>
    fun getCycleOfID(cycleID: Int?): Cycle
}