package com.example.lightweight.ui.cycleplanning.cycle

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Cycle
import com.example.lightweight.data.repositories.CycleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleViewModel(private val repository: CycleRepository) : ViewModel() {

    fun insert(cycle: Cycle) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycle)
    }

    fun delete(cycle: Cycle) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycle)
    }

    fun getAllCycles() = repository.getAllCycles()
}