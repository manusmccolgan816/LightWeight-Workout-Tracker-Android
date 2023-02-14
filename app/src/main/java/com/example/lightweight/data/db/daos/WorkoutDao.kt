package com.example.lightweight.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweight.data.db.entities.Workout
import java.time.LocalDate

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("DELETE FROM WORKOUT WHERE workout_ID = :workoutID")
    suspend fun deleteWorkoutOfID(workoutID: Int?)

    @Query("SELECT * FROM WORKOUT")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM WORKOUT WHERE date = :date")
    fun getWorkoutOfDate(date: String): Workout?

    @Query("SELECT date FROM WORKOUT ORDER BY date")
    fun getWorkoutDates(): LiveData<List<String>>

    @Query("SELECT W.workout_ID " +
            "FROM WORKOUT AS W " +
            "INNER JOIN EXERCISE_INSTANCE AS EI " +
            "ON W.workout_ID = EI.workout_ID " +
            "WHERE exercise_instance_ID = :exerciseInstanceID")
    fun getWorkoutOfExerciseInstance(exerciseInstanceID: Int?): Int?
}