package com.example.lightweight.ui.cycleplanning.cycledaycategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ICycleDayCategoryRepository

class CycleDayCategoryViewModelFactory(
    private val repository: ICycleDayCategoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CycleDayCategoryViewModel(repository) as T
    }
}