package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDay

class CycleDayRepository(private val db: WorkoutDatabase) : ICycleDayRepository {

    override suspend fun insert(cycleDay: CycleDay) = db.getCycleDayDao().insert(cycleDay)

    override suspend fun update(cycleDay: CycleDay) = db.getCycleDayDao().update(cycleDay)

    override suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int) =
        db.getCycleDayDao().decrementCycleDayNumbersAfter(cycleID, cycleDayNumber)

    override suspend fun delete(cycleDay: CycleDay) = db.getCycleDayDao().delete(cycleDay)

    override fun getCycleDayOfID(cycleDayID: Int?) = db.getCycleDayDao().getCycleDayOfID(cycleDayID)
}