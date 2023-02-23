package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDayExercise

class CycleDayExerciseRepository(private val db: WorkoutDatabase) {

    suspend fun insert(cycleDayExercise: CycleDayExercise) =
        db.getCycleDayExerciseDao().insert(cycleDayExercise)

    suspend fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?, cycleDayExerciseNumber: Int
    ) = db.getCycleDayExerciseDao()
        .decrementCycleDayExerciseNumbersAfter(cycleDayCategoryID, cycleDayExerciseNumber)

    suspend fun delete(cycleDayExercise: CycleDayExercise) =
        db.getCycleDayExerciseDao().delete(cycleDayExercise)

    suspend fun deleteOfID(cycleDayExerciseID: Int?) =
        db.getCycleDayExerciseDao().deleteOfID(cycleDayExerciseID)

    fun getAllCycleDayExercises() = db.getCycleDayExerciseDao().getAllCycleDayExercises()

    fun getCycleItemsOfCycleID(cycleID: Int?) =
        db.getCycleDayExerciseDao().getCycleItemsOfCycleID(cycleID)

    fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?) =
        db.getCycleDayExerciseDao().getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID)

    fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?) =
        db.getCycleDayExerciseDao().getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID)

    fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?) =
        db.getCycleDayExerciseDao().getCycleDayExerciseOfID(cycleDayExerciseID)
}