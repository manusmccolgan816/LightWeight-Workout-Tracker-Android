package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exercise: Exercise)

    @Query("UPDATE EXERCISE SET exercise_name = :exerciseName WHERE exercise_ID = :exerciseID")
    suspend fun updateName(exerciseID: Int?, exerciseName: String)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM EXERCISE  ORDER BY exercise_name")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM EXERCISE WHERE exercise_ID = :exerciseID")
    fun getExerciseOfID(exerciseID: Int?): Exercise

    @Query("SELECT * FROM EXERCISE WHERE category_ID = :categoryID ORDER BY exercise_name")
    fun getExercisesOfCategory(categoryID: Int?): LiveData<List<Exercise>>
}