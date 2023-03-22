package com.example.lightweight.ui.cycleplanning.cycleday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.CycleDayRepositoryInterface

class CycleDayViewModelFactory(
    private val repository: CycleDayRepositoryInterface
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CycleDayViewModel(repository) as T
    }
}