package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.ExerciseInstance

@Dao
interface ExerciseInstanceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exerciseInstance: ExerciseInstance)

    @Delete
    suspend fun delete(exerciseInstance: ExerciseInstance)

    @Query("SELECT * FROM EXERCISE_INSTANCE")
    fun getAllExerciseInstances(): LiveData<List<ExerciseInstance>>

    @Query("SELECT * FROM EXERCISE_INSTANCE WHERE workout_ID = :workoutID")
    fun getExerciseInstancesOfWorkout(workoutID: Int?): LiveData<List<ExerciseInstance>>

    @Query("SELECT * FROM EXERCISE_INSTANCE WHERE workout_ID = :workoutID AND exercise_ID = :exerciseID")
    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?): ExerciseInstance?
}