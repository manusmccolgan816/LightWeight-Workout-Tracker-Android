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

    @Query("UPDATE TRAINING_SET SET is_PR = :isPR WHERE training_set_ID = :trainingSetID")
    suspend fun updateIsPR(trainingSetID: Int?, isPR: Int)

    @Query("SELECT * FROM TRAINING_SET")
    fun getAllTrainingSets(): LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET " +
            "INNER JOIN EXERCISE_INSTANCE " +
            "ON TRAINING_SET.exercise_instance_ID = EXERCISE_INSTANCE.exercise_instance_ID " +
            "WHERE exercise_ID = :exerciseID AND is_PR = 1 " +
            "ORDER BY reps ASC")
    fun getPRTrainingSetsOfExercise(exerciseID: Int?): LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET " +
            "INNER JOIN EXERCISE_INSTANCE " +
            "ON TRAINING_SET.exercise_instance_ID = EXERCISE_INSTANCE.exercise_instance_ID " +
            "WHERE exercise_ID = :exerciseID AND is_PR = :isPR " +
            "ORDER BY reps ASC")
    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int): LiveData<List<TrainingSet>>

    @Query("SELECT * FROM TRAINING_SET WHERE exercise_instance_ID = :exerciseInstanceID")
    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET " +
            "INNER JOIN EXERCISE_INSTANCE " +
            "ON TRAINING_SET.exercise_instance_ID = EXERCISE_INSTANCE.exercise_instance_ID " +
            "WHERE exercise_ID = :exerciseID AND reps = :reps AND is_PR = :isPR " +
            "ORDER BY weight DESC")
    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int):
            LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET " +
            "INNER JOIN EXERCISE_INSTANCE " +
            "ON TRAINING_SET.exercise_instance_ID = EXERCISE_INSTANCE.exercise_instance_ID " +
            "WHERE exercise_ID = :exerciseID AND reps < :reps " +
            "ORDER BY reps DESC, weight DESC")
    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int)
}