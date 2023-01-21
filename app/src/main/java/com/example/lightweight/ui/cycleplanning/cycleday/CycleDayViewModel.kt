package com.example.lightweight.ui.cycleplanning.cycleday

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.data.repositories.CycleDayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleDayViewModel(private val repository: CycleDayRepository) : ViewModel() {

    fun insert(cycleDay: CycleDay) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycleDay)
    }

    fun delete(cycleDay: CycleDay) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycleDay)
    }

    fun getAllCycleDays() = repository.getAllCycleDays()
}