package com.example.lightweight.ui.cycleplanning.cycledaycategory

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.CycleDayCategory
import com.example.lightweight.data.repositories.CycleDayCategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleDayCategoryViewModel(private val repository: CycleDayCategoryRepository) : ViewModel() {

    fun insert(cycleDayCategory: CycleDayCategory) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycleDayCategory)
    }

    fun delete(cycleDayCategory: CycleDayCategory) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycleDayCategory)
    }

    fun getAllCycleDayCategories() = repository.getAllCycleDayCategories()

    fun getCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        repository.getCycleDayCategoriesOfCycleDay(cycleDayID)

    fun getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID: Int?) =
        repository.getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID)

    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        repository.getNumCycleDayCategoriesOfCycleDay(cycleDayID)
}