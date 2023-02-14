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

    @Update
    suspend fun update(trainingSet: TrainingSet)

    @Query("UPDATE TRAINING_SET SET is_PR = :isPR WHERE training_set_ID = :trainingSetID")
    suspend fun updateIsPR(trainingSetID: Int?, isPR: Int)

    @Query("UPDATE TRAINING_SET SET note = :note WHERE training_set_ID = :trainingSetID")
    suspend fun updateNote(trainingSetID: Int?, note: String?)

    @Query(
        "UPDATE TRAINING_SET " +
                "SET training_set_number = training_set_number - 1 " +
                "WHERE exercise_instance_ID = :exerciseInstanceID " +
                "AND training_set_number > :trainingSetNumber"
    )
    suspend fun decrementTrainingSetNumbersAbove(exerciseInstanceID: Int?, trainingSetNumber: Int)

    @Query("SELECT * FROM TRAINING_SET")
    fun getAllTrainingSets(): LiveData<List<TrainingSet>>

    @Query(
        "SELECT date FROM TRAINING_SET AS TS INNER JOIN " +
                "EXERCISE_INSTANCE AS EI " +
                "ON TS.exercise_instance_ID = EI.exercise_instance_ID " +
                "INNER JOIN WORKOUT AS W " +
                "ON EI.workout_ID = W.workout_ID " +
                "WHERE exercise_ID = :exerciseID AND is_PR = :isPR " +
                "ORDER BY reps"
    )
    fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int): LiveData<List<String>>

    @Query(
        "SELECT * " +
                "FROM TRAINING_SET AS TS " +
                "INNER JOIN EXERCISE_INSTANCE AS EI " +
                "ON TS.exercise_instance_ID = EI.exercise_instance_ID " +
                "WHERE exercise_ID = :exerciseID AND is_PR = :isPR " +
                "ORDER BY reps"
    )
    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int): LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET " +
            "WHERE exercise_instance_ID = :exerciseInstanceID " +
            "ORDER BY training_set_number")
    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?): LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET " +
            "WHERE exercise_instance_ID = :exerciseInstanceID " +
            "ORDER BY training_set_number")
    fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?): List<TrainingSet>

    @Query(
        "SELECT * " +
                "FROM TRAINING_SET AS TS " +
                "INNER JOIN EXERCISE_INSTANCE as EI " +
                "ON TS.exercise_instance_ID = EI.exercise_instance_ID " +
                "INNER JOIN WORKOUT AS W " +
                "ON EI.workout_ID = W.workout_ID " +
                "WHERE exercise_ID = :exerciseID AND reps = :reps AND is_PR = :isPR " +
                "ORDER BY weight DESC, date, training_set_number"
    )
    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int):
            LiveData<List<TrainingSet>>

    @Query(
        "SELECT * " +
                "FROM TRAINING_SET AS TS " +
                "INNER JOIN EXERCISE_INSTANCE AS EI " +
                "ON TS.exercise_instance_ID = EI.exercise_instance_ID " +
                "INNER JOIN WORKOUT AS W " +
                "ON EI.workout_ID = W.workout_ID " +
                "WHERE exercise_ID = :exerciseID AND reps < :reps " +
                "ORDER BY reps DESC, weight DESC, date, training_set_number"
    )
    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int): LiveData<List<TrainingSet>>

    @Query("SELECT * " +
            "FROM TRAINING_SET AS TS " +
            "INNER JOIN EXERCISE_INSTANCE AS EI " +
            "ON TS.exercise_instance_ID = EI.exercise_instance_ID " +
            "INNER JOIN WORKOUT AS W " +
            "ON EI.workout_ID = W.workout_ID " +
            "WHERE exercise_ID = :exerciseID " +
            "ORDER BY date, training_set_number")
    fun getTrainingSetsOfExercise(exerciseID: Int?): LiveData<List<TrainingSet>>
}