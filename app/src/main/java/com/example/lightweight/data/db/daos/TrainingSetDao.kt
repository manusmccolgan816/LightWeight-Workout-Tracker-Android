package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.TrainingSet

@Dao
interface TrainingSetDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(trainingSet: TrainingSet)

    @Delete
    suspend fun delete(trainingSet: TrainingSet)

    @Query("SELECT * FROM TRAINING_SET")
    fun getAllTrainingSets(): LiveData<List<TrainingSet>>
}