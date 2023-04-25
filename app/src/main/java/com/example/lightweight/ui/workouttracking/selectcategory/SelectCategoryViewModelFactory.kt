package com.example.lightweight.ui.workouttracking.selectcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightweight.data.repositories.ICategoryRepository

class SelectCategoryViewModelFactory(
    private val categoryRepository: ICategoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectCategoryViewModel(
            categoryRepository
        ) as T
    }
}