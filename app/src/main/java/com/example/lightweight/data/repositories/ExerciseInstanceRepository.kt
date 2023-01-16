package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.ExerciseInstance

class ExerciseInstanceRepository(private val db: WorkoutDatabase) {

    suspend fun insert(exerciseInstance: ExerciseInstance) = db.getExerciseInstanceDao()
        .insert(exerciseInstance)

    suspend fun delete(exerciseInstance: ExerciseInstance) = db.getExerciseInstanceDao()
        .delete(exerciseInstance)

    fun getAllExerciseInstances() = db.getExerciseInstanceDao().getAllExerciseInstances()

    fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesOfWorkoutNoLiveData(workoutID)

    fun getExerciseInstancesOfWorkout(workoutID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesOfWorkout(workoutID)

    fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesAndNamesOfWorkout(workoutID)

    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstance(workoutID, exerciseID)

    fun getExerciseInstancesOfExercise(exerciseID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesOfExercise(exerciseID)

    fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesAndDatesOfExercise(exerciseID)

    fun getExerciseInstanceOfID(exerciseInstanceID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstanceOfID(exerciseInstanceID)

    fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?) =
        db.getExerciseInstanceDao().getExerciseOfExerciseInstance(exerciseInstanceID)
}