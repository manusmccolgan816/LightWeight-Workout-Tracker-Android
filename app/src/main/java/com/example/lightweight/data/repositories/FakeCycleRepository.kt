package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Cycle

class FakeCycleRepository : ICycleRepository {

    private val cycles = mutableListOf<Cycle>()
    private val observableCycles = MutableLiveData<List<Cycle>>(cycles)

    private var lastId = 0

    private fun refreshLiveData() {
        observableCycles.postValue(cycles)
    }

    override suspend fun insert(cycle: Cycle) {
        if (cycle.cycleID == null) {
            cycle.cycleID = ++lastId
        }
        cycles.add(cycle)
        refreshLiveData()
    }

    override suspend fun update(cycle: Cycle) {
        cycles.removeIf { it.cycleID == cycle.cycleID }
        cycles.add(cycle)
        refreshLiveData()
    }

    override suspend fun delete(cycle: Cycle) {
        cycles.remove(cycle)
        refreshLiveData()
    }

    override fun getAllCycles(): LiveData<List<Cycle>> {
        return observableCycles
    }

    override fun getCycleOfID(cycleID: Int?): Cycle {
        return cycles.filter {
            it.cycleID == cycleID
        }[0]
    }
}