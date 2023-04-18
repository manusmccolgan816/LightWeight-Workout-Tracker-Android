package com.example.lightweight.ui.category

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.repositories.CategoryRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepositoryInterface) : ViewModel() {

    fun insert(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(category)
    }

    fun update(categoryID: Int?, newName: String) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(categoryID, newName)
    }

    fun delete(category: Category) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(category)
    }

    fun getAllCategories() = repository.getAllCategories()

    fun getCategoryOfID(categoryID: Int?) = repository.getCategoryOfID(categoryID)

    fun getCategoryOfIDObs(categoryID: Int?) = repository.getCategoryOfIDObs(categoryID)
}