package com.example.lightweight.ui.workouttracking.selectcategory

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.repositories.ICategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectCategoryViewModel(private val categoryRepository: ICategoryRepository) : ViewModel() {

    fun insert(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        categoryRepository.insert(category)
    }

    fun update(categoryID: Int?, newName: String) = CoroutineScope(Dispatchers.Main).launch {
        categoryRepository.update(categoryID, newName)
    }

    fun delete(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        categoryRepository.delete(category)
    }

    fun getAllCategories() = categoryRepository.getAllCategories()
}