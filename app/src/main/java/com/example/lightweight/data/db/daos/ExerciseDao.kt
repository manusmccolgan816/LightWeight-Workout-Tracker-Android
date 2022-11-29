package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exercise: Exercise)

    @Query("UPDATE EXERCISE SET exercise_name = :exerciseName WHERE exercise_ID = :exerciseID")
    suspend fun updateName(exerciseID: Int?, exerciseName: String)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM EXERCISE")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM EXERCISE WHERE category_ID = :categoryID")
    fun getExercisesOfCategory(categoryID: Int?): LiveData<List<Exercise>>
}