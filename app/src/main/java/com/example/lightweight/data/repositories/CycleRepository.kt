package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Cycle

class CycleRepository(private val db: WorkoutDatabase) {

    suspend fun insert(cycle: Cycle) = db.getCycleDao().insert(cycle)

    suspend fun update(cycle: Cycle) = db.getCycleDao().update(cycle)

    suspend fun delete(cycle: Cycle) = db.getCycleDao().delete(cycle)

    fun getAllCycles() = db.getCycleDao().getAllCycles()

    fun getCycleOfID(cycleID: Int?) = db.getCycleDao().getCycleOfID(cycleID)
}