package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.data.db.entities.CycleDay

interface CycleDayRepositoryInterface {
    suspend fun insert(cycleDay: CycleDay)
    suspend fun update(cycleDay: CycleDay)
    suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int)
    suspend fun delete(cycleDay: CycleDay)
    fun getCycleDayOfID(cycleDayID: Int?): LiveData<CycleDay>
}