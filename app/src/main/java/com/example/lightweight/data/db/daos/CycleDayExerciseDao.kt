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
}