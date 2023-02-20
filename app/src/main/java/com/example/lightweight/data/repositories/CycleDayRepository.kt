package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDay

class CycleDayRepository(private val db: WorkoutDatabase) {

    suspend fun insert(cycleDay: CycleDay) = db.getCycleDayDao().insert(cycleDay)

    suspend fun update(cycleDay: CycleDay) = db.getCycleDayDao().update(cycleDay)

    suspend fun delete(cycleDay: CycleDay) = db.getCycleDayDao().delete(cycleDay)

    fun getAllCycleDays() = db.getCycleDayDao().getAllCycleDays()

    fun getCycleDaysOfCycle(cycleID: Int?) = db.getCycleDayDao().getCycleDaysOfCycle(cycleID)
}