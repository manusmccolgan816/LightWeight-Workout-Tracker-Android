package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.CycleDay

class FakeCycleDayRepository : CycleDayRepositoryInterface {

    private val cycleDays = mutableListOf<CycleDay>()
    private val observableCycleDays = MutableLiveData<List<CycleDay>>(cycleDays)

    override suspend fun insert(cycleDay: CycleDay) {
        TODO("Not yet implemented")
    }

    override suspend fun update(cycleDay: CycleDay) {
        TODO("Not yet implemented")
    }

    override suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(cycleDay: CycleDay) {
        TODO("Not yet implemented")
    }

    override fun getCycleDayOfID(cycleDayID: Int?): LiveData<CycleDay> {
        TODO("Not yet implemented")
    }
}