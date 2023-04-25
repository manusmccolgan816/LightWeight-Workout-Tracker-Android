package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.CycleDayExercise

class CycleDayExerciseRepository(private val db: WorkoutDatabase) :
    ICycleDayExerciseRepository {

    override suspend fun insert(cycleDayExercise: CycleDayExercise) =
        db.getCycleDayExerciseDao().insert(cycleDayExercise)

    override suspend fun decrementCycleDayExerciseNumbersAfter(
        cycleDayCategoryID: Int?, cycleDayExerciseNumber: Int
    ) = db.getCycleDayExerciseDao()
        .decrementCycleDayExerciseNumbersAfter(cycleDayCategoryID, cycleDayExerciseNumber)

    override suspend fun delete(cycleDayExercise: CycleDayExercise) =
        db.getCycleDayExerciseDao().delete(cycleDayExercise)

    override suspend fun deleteOfID(cycleDayExerciseID: Int?) =
        db.getCycleDayExerciseDao().deleteOfID(cycleDayExerciseID)

    override fun getCycleItemsOfCycleID(cycleID: Int?) =
        db.getCycleDayExerciseDao().getCycleItemsOfCycleID(cycleID)

    override fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?) =
        db.getCycleDayExerciseDao().getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID)

    override fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?) =
        db.getCycleDayExerciseDao().getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID)

    override fun getCycleDayExerciseOfID(cycleDayExerciseID: Int?) =
        db.getCycleDayExerciseDao().getCycleDayExerciseOfID(cycleDayExerciseID)
}