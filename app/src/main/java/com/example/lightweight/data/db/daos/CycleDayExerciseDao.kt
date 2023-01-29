package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.CycleDayExercise

@Dao
interface CycleDayExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cycleDayExercise: CycleDayExercise)

    @Delete
    suspend fun delete(cycleDayExercise: CycleDayExercise)

    @Query("SELECT * FROM CYCLE_DAY_EXERCISE ORDER BY cycle_day_exercise_number")
    fun getAllCycleDayExercises(): LiveData<List<CycleDayExercise>>

    @Query("SELECT COUNT(cycle_day_exercise_number)" +
            "FROM CYCLE_DAY_EXERCISE " +
            "WHERE cycle_day_category_ID = :cycleDayCategoryID")
    fun getNumCycleDayExercisesOfCycleDayCategory(cycleDayCategoryID: Int?): LiveData<Int>
}