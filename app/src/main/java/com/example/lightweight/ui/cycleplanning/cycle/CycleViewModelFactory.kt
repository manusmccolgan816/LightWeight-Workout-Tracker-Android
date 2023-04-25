package com.example.lightweight.ui.cycleplanning.cycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ICycleRepository

class CycleViewModelFactory(
    private val repository: ICycleRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CycleViewModel(repository) as T
    }
}