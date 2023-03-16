package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.CycleDay

class FakeCycleDayRepository : CycleDayRepositoryInterface {

    private val cycleDays = mutableListOf<CycleDay>()
    private val observableCycleDays = MutableLiveData<List<CycleDay>>(cycleDays)

    private fun refreshLiveData() {
        observableCycleDays.postValue(cycleDays)
    }

    override suspend fun insert(cycleDay: CycleDay) {
        cycleDays.add(cycleDay)
        refreshLiveData()
    }

    override suspend fun update(cycleDay: CycleDay) {
        cycleDays.removeIf { it.cycleDayID == cycleDay.cycleDayID }
        cycleDays.add(cycleDay)
        refreshLiveData()
    }

    override suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int) {
        for (cycleDay in cycleDays.filter {
            it.cycleID == cycleID && it.cycleDayNumber > cycleDayNumber
        }) {
            cycleDay.cycleDayNumber--
            refreshLiveData()
        }
    }

    override suspend fun delete(cycleDay: CycleDay) {
        cycleDays.remove(cycleDay)
        refreshLiveData()
    }

    override fun getCycleDayOfID(cycleDayID: Int?): LiveData<CycleDay> {
        return MutableLiveData(cycleDays.filter { it.cycleDayID == cycleDayID }[0])
    }
}