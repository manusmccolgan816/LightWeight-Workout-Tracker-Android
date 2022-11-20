package com.example.lightweight.ui.category

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.repositories.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: WorkoutRepository)
    : ViewModel() {

    fun upsert(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(category)
    }

    fun delete(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(category)
    }

    // Doesn't need to be executed in a coroutine because it is just a read operation
    fun getAllCategories() = repository.getAllCategories()
}