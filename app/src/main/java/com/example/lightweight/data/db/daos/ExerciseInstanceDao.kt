package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.IdNamePair
import com.example.lightweight.data.db.entities.ExerciseInstance

@Dao
interface ExerciseInstanceDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exerciseInstance: ExerciseInstance)

    @Query("UPDATE EXERCISE_INSTANCE " +
            "SET exercise_instance_number = :eiNumber " +
            "WHERE exercise_instance_ID = :exerciseInstanceID")
    suspend fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int)

    @Query("UPDATE EXERCISE_INSTANCE " +
            "SET exercise_instance_number = exercise_instance_number - 1 " +
            "WHERE (SELECT W.workout_ID " +
            "       FROM EXERCISE_INSTANCE AS EI " +
            "       INNER JOIN WORKOUT AS W " +
            "       ON EI.workout_ID = W.workout_ID) = :workoutID " +
            "AND exercise_instance_number > :eiNumber")
    suspend fun decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID: Int?, eiNumber: Int)

    @Delete
    suspend fun delete(exerciseInstance: ExerciseInstance)

    @Query("SELECT * FROM EXERCISE_INSTANCE")
    fun getAllExerciseInstances(): LiveData<List<ExerciseInstance>>

    @Query(
        "SELECT * " +
                "FROM EXERCISE_INSTANCE " +
                "WHERE workout_ID = :workoutID"
    )
    fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?): List<ExerciseInstance>

    @Query(
        "SELECT * " +
                "FROM EXERCISE_INSTANCE " +
                "WHERE workout_ID = :workoutID " +
                "ORDER BY exercise_instance_number"
    )
    fun getExerciseInstancesOfWorkout(workoutID: Int?): LiveData<List<ExerciseInstance>>

    @Query(
        "SELECT EI.exercise_instance_ID AS id, E.exercise_name AS name " +
                "FROM EXERCISE_INSTANCE AS EI " +
                "INNER JOIN EXERCISE AS E " +
                "ON EI.exercise_ID = E.exercise_ID " +
                "WHERE workout_ID = :workoutID " +
                "ORDER BY exercise_instance_number"
    )
    fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?): LiveData<List<IdNamePair>>

    @Query("SELECT * FROM EXERCISE_INSTANCE WHERE workout_ID = :workoutID AND exercise_ID = :exerciseID")
    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?): ExerciseInstance?

    @Query("SELECT * FROM EXERCISE_INSTANCE WHERE exercise_ID = :exerciseID")
    fun getExerciseInstancesOfExercise(exerciseID: Int?): LiveData<List<ExerciseInstance>>

    @MapInfo(keyColumn = "exercise_instance_ID", valueColumn = "date")
    @Query(
        "SELECT EI.exercise_instance_ID, W.date " +
                "FROM EXERCISE_INSTANCE AS EI " +
                "INNER JOIN WORKOUT AS W " +
                "ON EI.workout_ID = W.workout_ID " +
                "WHERE exercise_ID = :exerciseID " +
                "ORDER BY date DESC"
    )
    fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?): LiveData<Map<Int?, String>>

    @Query("SELECT * FROM EXERCISE_INSTANCE WHERE exercise_instance_ID = :exerciseInstanceID")
    fun getExerciseInstanceOfID(exerciseInstanceID: Int?): ExerciseInstance

    @Query(
        "SELECT exercise_ID " +
                "FROM EXERCISE_INSTANCE " +
                "WHERE exercise_instance_ID = :exerciseInstanceID"
    )
    fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?): LiveData<Int?>
}