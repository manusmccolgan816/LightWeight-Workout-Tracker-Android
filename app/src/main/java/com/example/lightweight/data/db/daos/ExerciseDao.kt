package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM EXERCISE")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM EXERCISE WHERE category_ID = :categoryID")
    fun getExercisesOfCategory(categoryID: Int?): LiveData<List<Exercise>>
}