package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.CycleDay

class FakeCycleDayRepository : CycleDayRepositoryInterface {

    private val allTag = 0
    private val cycleDayOfIDTag = 1

    private val cycleDays = mutableListOf<CycleDay>()
    private val observableCycleDays = MutableLiveData<List<CycleDay>>(cycleDays)

    private val observableCycleDayOfID = MutableLiveData<CycleDay>()
    private var cycleDayOfIDParam: Int? = null

    private fun refreshLiveData(tag: Int?) {
        if (tag == allTag) observableCycleDays.postValue(cycleDays)
        if (tag == allTag || tag == cycleDayOfIDTag) {
            if (cycleDayOfIDParam != null) {
                observableCycleDayOfID.postValue(calcCycleDayOfID(cycleDayOfIDParam))
            }
        }
    }

    override suspend fun insert(cycleDay: CycleDay) {
        cycleDays.add(cycleDay)
        refreshLiveData(allTag)
    }

    override suspend fun update(cycleDay: CycleDay) {
        cycleDays.removeIf { it.cycleDayID == cycleDay.cycleDayID }
        cycleDays.add(cycleDay)
        refreshLiveData(allTag)
    }

    override suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int) {
        for (cycleDay in cycleDays.filter {
            it.cycleID == cycleID && it.cycleDayNumber > cycleDayNumber
        }) {
            cycleDay.cycleDayNumber--
            refreshLiveData(allTag)
        }
    }

    override suspend fun delete(cycleDay: CycleDay) {
        cycleDays.remove(cycleDay)
        refreshLiveData(allTag)
    }

    override fun getCycleDayOfID(cycleDayID: Int?): LiveData<CycleDay> {
        cycleDayOfIDParam = cycleDayID
        refreshLiveData(cycleDayOfIDTag)
        return observableCycleDayOfID
    }

    private fun calcCycleDayOfID(cycleDayID: Int?): CycleDay {
        return cycleDays.filter { it.cycleDayID == cycleDayID }[0]
    }
}