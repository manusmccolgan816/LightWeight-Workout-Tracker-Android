package com.example.lightweight.ui.cycleplanning.cycleday

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.data.repositories.CycleDayRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleDayViewModel(private val repository: CycleDayRepositoryInterface) : ViewModel() {

    fun insert(cycleDay: CycleDay) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycleDay)
    }

    fun update(cycleDay: CycleDay) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(cycleDay)
    }

    fun decrementCycleDayNumbersAfter(cycleID: Int?, cycleDayNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            repository.decrementCycleDayNumbersAfter(cycleID, cycleDayNumber)
        }

    fun delete(cycleDay: CycleDay) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycleDay)
    }

    fun getCycleDayOfID(cycleDayID: Int?) = repository.getCycleDayOfID(cycleDayID)
}