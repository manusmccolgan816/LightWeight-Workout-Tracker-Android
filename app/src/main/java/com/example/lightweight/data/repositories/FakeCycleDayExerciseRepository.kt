package com.example.lightweight.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.*
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryExerciseCombo
import com.example.lightweight.ui.cycleplanning.CycleItem
import java.lang.IndexOutOfBoundsException

class FakeCycleDayExerciseRepository : CycleDayExerciseRepositoryInterface {

    private val allTag = 0
    private val cycleItemsOfCycleIDTag = 1
    private val numCycleDayExercisesOfCycleDayCategoryTag = 2
    private val cycleDayExerciseOfIDTag = 3

    var categories = mutableListOf<Category>()
    var exercises = mutableListOf<Exercise>()
    var cycleDays = mutableListOf<CycleDay>()
    var cycleDayCategories = mutableListOf<CycleDayCategory>()

    private val cycleDayExercises = mutableListOf<CycleDayExercise>()
    private val observableCycleDayExercises =
        MutableLiveData<List<CycleDayExercise>>(cycleDayExercises)

    private val observableCycleItemsOfCycleID = MutableLiveData<List<CycleItem>>()
    private var cycleItemsOfCycleIDParam: Int? = null

    private val observableNumCycleDayExercisesOfCycleDayCategory = MutableLiveData<Int>()
    private var numCycleDayExercisesOfCycleDayCategoryParam: Int? = null

    private val observableCycleDayExerciseOfID = MutableLiveData<CycleDayExercise>()
    private var cycleDayExerciseOfIDParam: Int? = null

    private var lastId = 0

    fun refreshLiveData(tag: Int) {
        if (tag == allTag) observableCycleDayExercises.postValue(cycleDayExercises)
        if (tag == allTag || tag == cycleItemsOfCycleIDTag) {
            if (cycleItemsOfCycleIDParam != null) {
                observableCycleItemsOfCycleID.postValue(
                    calcCycleItemsOfCycleID(cycleItemsOfCycleIDParam)
                )
            }
        }
        if (tag == allTag || tag == numCycleDayExercisesOfCycleDayCategoryTag) {
            if (numCycleDayExercisesOfCycleDayCategoryParam != null) {
                observableNumCycleDayExercisesOfCycleDayCategory.postValue(
                    calcNumCycleDayExercisesOfCycleDayCategory(
                        numCycleDayExercisesOfCycleDayCategoryParam
                    )
                )
            }
        }
        if (tag == allTag || tag == cycleDayExerciseOfIDTag) {
            if (cycleDayExerciseOfIDParam != null) {
                observableCycleDayExerciseOfID.postValue(
                    calcCycleDayExerciseOfID(cycleDayExerciseOfIDParam)
                )
            }
        }
    }

    override suspend fun insert(cycleDayExercise: CycleDayExercise) {
        if (cycleDayExercise.cycleDayExerciseID == null) {
            cycleDayExercise.cycleDayExerciseID = ++lastId
        }
        cycleDayExercises.add(cycleDayExercise)
        refreshLiveData(allTag)
    }

    override suspend fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?,
        cycleDayExerciseNumber: Int
    ) {
        for (cycleDayExercise in cycleDayExercises.filter {
            it.cycleDayCategoryID == cycleDayCategoryID
                    && it.cycleDayExerciseNumber > cycleDayExerciseNumber
        }) {
            cycleDayExercise.cycleDayExerciseNumber--
            refreshLiveData(allTag)
        }
    }

    override suspend fun delete(cycleDayExercise: CycleDayExercise) {
        cycleDayExercises.remove(cycleDayExercise)
        refreshLiveData(allTag)
    }

    override suspend fun deleteOfID(cycleDayExerciseID: Int?) {
        cycleDayExercises.removeIf { it.cycleDayExerciseID == cycleDayExerciseID }
        refreshLiveData(allTag)
    }

    override fun getCycleItemsOfCycleID(cycleID: Int?): LiveData<List<CycleItem>> {
        cycleItemsOfCycleIDParam = cycleID
        refreshLiveData(cycleItemsOfCycleIDTag)
        return observableCycleItemsOfCycleID
    }

    private fun calcCycleItemsOfCycleID(cycleID: Int?): List<CycleItem> {
        val itemsToReturn = arrayListOf<CycleItem>()

        for (cycleDay in cycleDays.filter {
            it.cycleID == cycleID
        }.sortedBy { it.cycleDayNumber }) {

            // Add the cycle item now if cycleDay has no children
            if (cycleDayCategories.none { it.cycleDayID == cycleDay.cycleDayID }) {
                itemsToReturn.add(
                    CycleItem(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        cycleDay.cycleDayID,
                        cycleDay.cycleDayName,
                        cycleDay.cycleDayNumber
                    )
                )
            }

            for (cycleDayCategory in cycleDayCategories.filter {
                it.cycleDayID == cycleDay.cycleDayID
            }.sortedBy { it.cycleDayCategoryNumber }) {
                for (category in categories.filter {
                    it.categoryID == cycleDayCategory.categoryID
                }) {

                    // Add the cycle item now if the cycleDayCategory has no children
                    if (cycleDayExercises.none {
                            it.cycleDayCategoryID == cycleDayCategory.cycleDayCategoryID
                        }) {
                        itemsToReturn.add(
                            CycleItem(
                                null,
                                null,
                                null,
                                cycleDayCategory.cycleDayCategoryID,
                                cycleDayCategory.cycleDayCategoryNumber,
                                category.categoryName,
                                cycleDay.cycleDayID,
                                cycleDay.cycleDayName,
                                cycleDay.cycleDayNumber
                            )
                        )
                    }
                    for (cycleDayExercise in cycleDayExercises.filter {
                        it.cycleDayCategoryID == cycleDayCategory.cycleDayCategoryID
                    }.sortedBy { it.cycleDayExerciseNumber }) {
                        for (exercise in exercises.filter {
                            it.exerciseID == cycleDayExercise.cycleDayExerciseID
                        }) {
                            itemsToReturn.add(
                                CycleItem(
                                    cycleDayExercise.cycleDayExerciseID,
                                    cycleDayExercise.cycleDayExerciseNumber,
                                    exercise.exerciseName,
                                    cycleDayCategory.cycleDayCategoryID,
                                    cycleDayCategory.cycleDayCategoryNumber,
                                    category.categoryName,
                                    cycleDay.cycleDayID,
                                    cycleDay.cycleDayName,
                                    cycleDay.cycleDayNumber
                                )
                            )
                        }
                    }
                }
            }
        }

        Log.d("FakeCycleDayExerciseRepository", "cycleItemsOfCycleID size = ${itemsToReturn.size}")
        return itemsToReturn
    }

    override fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): LiveData<Int> {
        numCycleDayExercisesOfCycleDayCategoryParam = cycleDayCategoryID
        refreshLiveData(numCycleDayExercisesOfCycleDayCategoryTag)
        return observableNumCycleDayExercisesOfCycleDayCategory
    }

    private fun calcNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): Int {
        return cycleDayExercises.filter { it.cycleDayCategoryID == cycleDayCategoryID }.size
    }

    override fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?):
            List<CycleDayCategoryExerciseCombo> {
        val combosAndOrder = arrayListOf<Pair<CycleDayCategoryExerciseCombo, Int>>()

        for (cycleDayCategory in cycleDayCategories.filter {
            it.cycleDayCategoryID == cycleDayCategoryID
        }) {
            for (cycleDayExercise in cycleDayExercises.filter {
                it.cycleDayCategoryID == cycleDayCategoryID
            }) {
                for (exercise in exercises.filter {
                    it.exerciseID == cycleDayExercise.exerciseID
                }) {
                    combosAndOrder.add(
                        Pair(
                            CycleDayCategoryExerciseCombo(
                                cycleDayExercise.cycleDayExerciseID,
                                exercise.exerciseName,
                                cycleDayCategoryID
                            ),
                            cycleDayExercise.cycleDayExerciseNumber
                        )
                    )
                }
            }
        }

        // Sort by cycleDayExerciseNumber
        combosAndOrder.sortBy { it.second }
        val combosToReturn = arrayListOf<CycleDayCategoryExerciseCombo>()
        for (combo in combosAndOrder) {
            combosToReturn.add(combo.first)
        }

        return combosToReturn
    }

    override fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?): LiveData<CycleDayExercise> {
        cycleDayExerciseOfIDParam = cycleDayExerciseID
        refreshLiveData(cycleDayExerciseOfIDTag)
        return observableCycleDayExerciseOfID
    }

    private fun calcCycleDayExerciseOfID(cycleDayExerciseID: Int?): CycleDayExercise? {
        return try {
            cycleDayExercises.filter { it.cycleDayExerciseID == cycleDayExerciseID }[0]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }
}