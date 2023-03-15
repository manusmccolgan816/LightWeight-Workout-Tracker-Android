package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Cycle

class CycleRepository(private val db: WorkoutDatabase) : CycleRepositoryInterface {

    override suspend fun insert(cycle: Cycle) = db.getCycleDao().insert(cycle)

    override suspend fun update(cycle: Cycle) = db.getCycleDao().update(cycle)

    override suspend fun delete(cycle: Cycle) = db.getCycleDao().delete(cycle)

    override fun getAllCycles() = db.getCycleDao().getAllCycles()

    override fun getCycleOfID(cycleID: Int?) = db.getCycleDao().getCycleOfID(cycleID)
}