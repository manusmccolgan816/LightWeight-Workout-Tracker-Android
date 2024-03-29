package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryCombo
import com.example.lightweight.data.db.entities.CycleDayCategory
import java.lang.IndexOutOfBoundsException

class FakeCycleDayCategoryRepository : ICycleDayCategoryRepository {

    private val allTag = 0
    private val categoryIDOfCycleDayCategoryIDTag = 1
    private val numCycleDayCategoriesOfCycleDayTag = 2
    private val cycleDayCategoryOfIDTag = 3

    var cycleDayExerciseRepo: FakeCycleDayExerciseRepository? = null

    var categories = mutableListOf<Category>()
    var cycleDays = mutableListOf<CycleDay>()

    private val cycleDayCategories = mutableListOf<CycleDayCategory>()
    private val observableCycleDayCategories =
        MutableLiveData<List<CycleDayCategory>>(cycleDayCategories)

    private val observableCategoryIDOfCycleDayCategoryID = MutableLiveData<Int?>()
    private var categoryIDOfCycleDayCategoryIDParam: Int? = null

    private val observableNumCycleDayCategoriesOfCycleDay = MutableLiveData<Int>()
    private var numCycleDayCategoriesOfCycleDayParam: Int? = null

    private val observableCycleDayCategoryOfID = MutableLiveData<CycleDayCategory>()
    private var cycleDayCategoryOfIDParam: Int? = null

    private var lastId = 0

    fun refreshLiveData(tag: Int) {
        if (tag == allTag) observableCycleDayCategories.postValue(cycleDayCategories)
        if (tag == allTag || tag == categoryIDOfCycleDayCategoryIDTag) {
            if (categoryIDOfCycleDayCategoryIDParam != null) {
                observableCategoryIDOfCycleDayCategoryID.postValue(
                    calcCategoryIDOfCycleDayCategoryID(categoryIDOfCycleDayCategoryIDParam)
                )
            }
        }
        if (tag == allTag || tag == numCycleDayCategoriesOfCycleDayTag) {
            if (numCycleDayCategoriesOfCycleDayParam != null) {
                observableNumCycleDayCategoriesOfCycleDay.postValue(
                    calcNumCycleDayCategoriesOfCycleDay(numCycleDayCategoriesOfCycleDayParam)
                )
            }
        }
        if (tag == allTag || tag == cycleDayCategoryOfIDTag) {
            if (cycleDayCategoryOfIDParam != null) {
                observableCycleDayCategoryOfID.postValue(
                    calcCycleDayCategoryOfID(cycleDayCategoryOfIDParam!!)
                )
            }
        }
    }

    override suspend fun insert(cycleDayCategory: CycleDayCategory) {
        if (cycleDayCategory.cycleDayCategoryID == null) {
            cycleDayCategory.cycleDayCategoryID = ++lastId
        }
        cycleDayCategories.add(cycleDayCategory)
        refreshLiveData(allTag)

        // Notify cycleDayExerciseRepo that cycleDay has been inserted
        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.cycleDayCategories?.add(cycleDayCategory)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun decrementCycleDayCategoryNumbersAfter(
        cycleDayID: Int?,
        cycleDayCategoryNumber: Int
    ) {
        for (cycleDayCategory in cycleDayCategories.filter {
            it.cycleDayID == cycleDayID && it.cycleDayCategoryNumber > cycleDayCategoryNumber
        }) {
            cycleDayCategory.cycleDayCategoryNumber--
            refreshLiveData(allTag)
        }

        // TODO Notify cycleDayExerciseRepo
    }

    override suspend fun delete(cycleDayCategory: CycleDayCategory) {
        cycleDayCategories.remove(cycleDayCategory)
        refreshLiveData(allTag)

        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.cycleDayCategories?.remove(cycleDayCategory)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun deleteOfID(cycleDayCategoryID: Int?) {
        cycleDayCategories.removeIf { it.cycleDayCategoryID == cycleDayCategoryID }
        refreshLiveData(allTag)

        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.cycleDayCategories?.removeIf {
                it.cycleDayCategoryID == cycleDayCategoryID
            }
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
    }


    override fun getCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?): LiveData<Int?> {
        categoryIDOfCycleDayCategoryIDParam = cycleDayCategoryID
        refreshLiveData(categoryIDOfCycleDayCategoryIDTag)
        return observableCategoryIDOfCycleDayCategoryID
    }

    private fun calcCategoryIDOfCycleDayCategoryID(cycleDayCategoryID: Int?): Int? {
        return cycleDayCategories.filter { it.cycleDayCategoryID == cycleDayCategoryID }[0].categoryID
    }

    override fun getNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): LiveData<Int> {
        numCycleDayCategoriesOfCycleDayParam = cycleDayID
        refreshLiveData(numCycleDayCategoriesOfCycleDayTag)
        return observableNumCycleDayCategoriesOfCycleDay
    }

    private fun calcNumCycleDayCategoriesOfCycleDay(cycleDayID: Int?): Int {
        return cycleDayCategories.filter { it.cycleDayID == cycleDayID }.size
    }

    override fun getCycleDayCategoryOfID(cycleDayCategoryID: Int?): LiveData<CycleDayCategory> {
        cycleDayCategoryOfIDParam = cycleDayCategoryID
        refreshLiveData(cycleDayCategoryOfIDTag)
        return observableCycleDayCategoryOfID
    }
    override fun getCycleDayCatCombosOfCycle(cycleID: Int?): List<CycleDayCategoryCombo> {
        val combosAndOrder = arrayListOf<Pair<CycleDayCategoryCombo, Int>>()

        for (cycleDay in cycleDays.filter { it.cycleID == cycleID }) {
            for (cycleDayCategory in cycleDayCategories.filter {
                it.cycleDayID == cycleDay.cycleDayID
            }) {
                for (category in categories.filter {
                    it.categoryID == cycleDayCategory.cycleDayCategoryID
                }) {
                    combosAndOrder.add(
                        Pair(
                            CycleDayCategoryCombo(
                                cycleDayCategory.cycleDayCategoryID,
                                category.categoryName,
                                cycleDay.cycleDayID
                            ), cycleDayCategory.cycleDayCategoryNumber
                        )
                    )
                }
            }
        }

        // Sort by cycleDayCategoryNumber
        combosAndOrder.sortBy { it.second }
        val combosToReturn = arrayListOf<CycleDayCategoryCombo>()
        for (combo in combosAndOrder) {
            combosToReturn.add(combo.first)
        }

        return combosToReturn
    }

    private fun calcCycleDayCategoryOfID(cycleDayCategoryID: Int): CycleDayCategory? {
        return try {
            cycleDayCategories.filter { it.cycleDayCategoryID == cycleDayCategoryID }[0]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }
}