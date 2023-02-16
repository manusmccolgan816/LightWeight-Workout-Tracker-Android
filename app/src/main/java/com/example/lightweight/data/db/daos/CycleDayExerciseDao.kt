package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.CycleDayCategoryExerciseCombo
import com.example.lightweight.CycleItem
import com.example.lightweight.data.db.entities.CycleDayExercise

@Dao
interface CycleDayExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycleDayExercise: CycleDayExercise)

    @Delete
    suspend fun delete(cycleDayExercise: CycleDayExercise)

    @Query("DELETE FROM CYCLE_DAY_EXERCISE WHERE cycle_day_exercise_ID = :cycleDayExerciseID")
    suspend fun deleteOfID(cycleDayExerciseID: Int?)

    @Query("SELECT * FROM CYCLE_DAY_EXERCISE ORDER BY cycle_day_exercise_number")
    fun getAllCycleDayExercises(): LiveData<List<CycleDayExercise>>

    @Query("SELECT CDE.cycle_day_exercise_ID, CDE.cycle_day_exercise_number, E.exercise_name, " +
            "CDC.cycle_day_category_ID, CDC.cycle_day_category_number, C.category_name, " +
            "CD.cycle_day_ID, CD.cycle_day_name, CD.cycle_day_number " +
            "FROM CYCLE_DAY AS CD " +
            "LEFT JOIN CYCLE_DAY_CATEGORY AS CDC " +
            "ON CD.cycle_day_ID = CDC.cycle_day_ID " +
            "LEFT JOIN CATEGORY AS C " +
            "ON CDC.category_ID = C.category_ID " +
            "LEFT JOIN CYCLE_DAY_EXERCISE AS CDE " +
            "ON CDC.cycle_day_category_ID = CDE.cycle_day_category_ID " +
            "LEFT JOIN EXERCISE AS E " +
            "ON CDE.exercise_ID = E.exercise_ID " +
            "WHERE cycle_ID = :cycleID " +
            "ORDER BY cycle_day_number, cycle_day_category_number, cycle_day_exercise_number")
    fun getCycleItemsOfCycleID(cycleID: Int?): LiveData<List<CycleItem>>

    @Query("SELECT COUNT(cycle_day_exercise_number)" +
            "FROM CYCLE_DAY_EXERCISE " +
            "WHERE cycle_day_category_ID = :cycleDayCategoryID")
    fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): LiveData<Int>

    @Query("SELECT CDE.cycle_day_exercise_ID, E.exercise_name, CDC.cycle_day_category_ID " +
            "FROM CYCLE_DAY_CATEGORY AS CDC " +
            "INNER JOIN CYCLE_DAY_EXERCISE AS CDE " +
            "ON CDC.cycle_day_category_ID = CDE.cycle_day_category_ID " +
            "INNER JOIN EXERCISE AS E " +
            "ON CDE.exercise_ID = E.exercise_ID " +
            "WHERE CDC.cycle_day_category_ID = :cycleDayCategoryID " +
            "ORDER BY cycle_day_exercise_number")
    fun getCycleDayCatExCombosOfCycleDayCategory(cycleDayCategoryID: Int?): List<CycleDayCategoryExerciseCombo>
}