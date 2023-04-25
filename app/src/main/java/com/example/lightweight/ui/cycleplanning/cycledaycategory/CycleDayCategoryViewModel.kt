package com.example.lightweight.ui.cycleplanning.cycledaycategory

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.CycleDayCategory
import com.example.lightweight.data.repositories.ICycleDayCategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleDayCategoryViewModel(private val repository: ICycleDayCategoryRepository) :
    ViewModel() {

    fun insert(cycleDayCategory: CycleDayCategory) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(cycleDayCategory)
    }

    fun decrementCycleDayCategoryNumbersAfter(cycleDayID: Int?, cycleDayCategoryNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            repository.decrementCycleDayCategoryNumbersAfter(cycleDayID, cycleDayCategoryNumber)
        }

    fun delete(cycleDayCategory: CycleDayCategory) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(cycleDayCategory)
    }

    fun deleteOfID(cycleDayCategoryID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteOfID(cycleDayCategoryID)
    }

    fun getCycleDayCatCombosOfCycle(cycleID: Int?) =
        repository.getCycleDayCatCombosOfCycle(cycleID)

    fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?) =
        repository.getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID)

    fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?) =
        repository.getNumCycleDayCategoriesOfCycleDay(cycleDayID)

    fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?) =
        repository.getCycleDayCategoryOfID(cycleDayCategoryID)
}