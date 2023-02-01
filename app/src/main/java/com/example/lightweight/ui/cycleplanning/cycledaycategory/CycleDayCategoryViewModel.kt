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

    fun deleteOfID(cycleDayCategoryID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteOfID(cycleDayCategoryID)
    }

    fun getAllCycleDayCategories() = repository.getAllCycleDayCategories()

    fun getCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        repository.getCycleDayCategoriesOfCycleDay(cycleDayID)

    fun getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID: Int?) =
        repository.getCycleDayCategoriesAndNamesOfCycleDay(cycleDayID)

    fun getCycleDayCategoriesNamesCycleDaysOfCycleDay(cycleDayID: Int?) =
        repository.getCycleDayCategoriesNamesCycleDaysOfCycleDay(cycleDayID)

    fun getCycleDayAndCycleDayCategoriesOfCycle(cycleID: Int?) =
        repository.getCycleDayAndCycleDayCategoriesOfCycle(cycleID)

    fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?) =
        repository.getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID)

    fun getCycleDayIDOfCycleDayCategoryID(cycleDayCategoryID: Int?) =
        repository.getCycleDayIDOfCycleDayCategoryID(cycleDayCategoryID)

    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        repository.getNumCycleDayCategoriesOfCycleDay(cycleDayID)
}