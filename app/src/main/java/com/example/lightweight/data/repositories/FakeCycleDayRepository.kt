package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.CycleDay

class FakeCycleDayRepository : CycleDayRepositoryInterface {

    private val allTag = 0
    private val cycleDayOfIDTag = 1

    var cycleDayCategoryRepo: FakeCycleDayCategoryRepository? = null
    var cycleDayExerciseRepo: FakeCycleDayExerciseRepository? = null

    private val cycleDays = mutableListOf<CycleDay>()
    private val observableCycleDays = MutableLiveData<List<CycleDay>>(cycleDays)

    private val observableCycleDayOfID = MutableLiveData<CycleDay>()
    private var cycleDayOfIDParam: Int? = null

    private var lastId = 0

    private fun refreshLiveData(tag: Int?) {
        if (tag == allTag) {
            observableCycleDays.postValue(cycleDays)
        }
        if (tag == allTag || tag == cycleDayOfIDTag) {
            if (cycleDayOfIDParam != null) {
                observableCycleDayOfID.postValue(calcCycleDayOfID(cycleDayOfIDParam))
            }
        }
    }

    override suspend fun insert(cycleDay: CycleDay) {
        if (cycleDay.cycleDayID == null) {
            cycleDay.cycleDayID = ++lastId
        }
        cycleDays.add(cycleDay)
        refreshLiveData(allTag)

        // Notify cycleDayCategoryRepo that cycleDay has been inserted
        if (cycleDayCategoryRepo != null) {
            cycleDayCategoryRepo?.cycleDays?.add(cycleDay)
            cycleDayCategoryRepo?.refreshLiveData(allTag)
        }
        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.cycleDays?.add(cycleDay)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun update(cycleDay: CycleDay) {
        cycleDays.removeIf { it.cycleDayID == cycleDay.cycleDayID }
        cycleDays.add(cycleDay)
        refreshLiveData(allTag)

        // TODO Notify cycleDayExerciseRepo
    }

    override suspend fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int) {
        for (cycleDay in cycleDays.filter {
            it.cycleID == cycleID && it.cycleDayNumber > cycleDayNumber
        }) {
            cycleDay.cycleDayNumber--
            refreshLiveData(allTag)
        }

        // TODO Notify cycleDayExerciseRepo
    }

    override suspend fun delete(cycleDay: CycleDay) {
        cycleDays.remove(cycleDay)
        refreshLiveData(allTag)

        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.cycleDays?.remove(cycleDay)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
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