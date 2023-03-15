package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Cycle

class FakeCycleRepository : CycleRepositoryInterface {

    private val cycles = mutableListOf<Cycle>()
    private val observableCycles = MutableLiveData<List<Cycle>>(cycles)

    override suspend fun insert(cycle: Cycle) {
        TODO("Not yet implemented")
    }

    override suspend fun update(cycle: Cycle) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(cycle: Cycle) {
        TODO("Not yet implemented")
    }

    override fun getAllCycles(): LiveData<List<Cycle>> {
        TODO("Not yet implemented")
    }

    override fun getCycleOfID(cycleID: Int?): Cycle {
        TODO("Not yet implemented")
    }
}