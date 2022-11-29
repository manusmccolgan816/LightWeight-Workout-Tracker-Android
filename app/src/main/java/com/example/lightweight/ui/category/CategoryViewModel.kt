package com.example.lightweight.ui.category

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.repositories.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    fun insert(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(category)
    }

    fun update(categoryID: Int?, newName: String) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(categoryID, newName)
    }

    fun delete(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(category)
    }

    // Doesn't need to be executed in a coroutine because it is just a read operation
    fun getAllCategories() = repository.getAllCategories()

    fun getCategoryOfID(categoryID: Int?) = repository.getCategoryOfID(categoryID)
}