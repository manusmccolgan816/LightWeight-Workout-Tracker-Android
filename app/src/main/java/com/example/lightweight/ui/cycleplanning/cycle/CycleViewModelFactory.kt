package com.example.lightweight.ui.cycleplanning.cycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.CycleRepositoryInterface

class CycleViewModelFactory(
    private val repository: CycleRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CycleViewModel(repository) as T
    }
}