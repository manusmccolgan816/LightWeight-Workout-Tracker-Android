package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.ExerciseInstance

class ExerciseInstanceRepository(private val db: WorkoutDatabase) :
    ExerciseInstanceRepositoryInterface {

    override suspend fun insert(exerciseInstance: ExerciseInstance) = db.getExerciseInstanceDao()
        .insert(exerciseInstance)

    override suspend fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) =
        db.getExerciseInstanceDao().updateExerciseInstanceNumber(exerciseInstanceID, eiNumber)

    override suspend fun decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID: Int?, eiNumber: Int) =
        db.getExerciseInstanceDao()
            .decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID, eiNumber)

    override suspend fun delete(exerciseInstance: ExerciseInstance) = db.getExerciseInstanceDao()
        .delete(exerciseInstance)

    override fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesOfWorkoutNoLiveData(workoutID)

    override fun getExerciseInstancesOfWorkout(workoutID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesOfWorkout(workoutID)

    override fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesAndNamesOfWorkout(workoutID)

    override fun getExerciseInstance(workoutID: Int?, exerciseID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstance(workoutID, exerciseID)

    override fun getExerciseInstancesOfExercise(exerciseID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesOfExercise(exerciseID)

    override fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstancesAndDatesOfExercise(exerciseID)

    override fun getExerciseInstanceOfID(exerciseInstanceID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstanceOfID(exerciseInstanceID)

    override fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstanceOfIDObs(exerciseInstanceID)

    override fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?) =
        db.getExerciseInstanceDao().getExerciseOfExerciseInstance(exerciseInstanceID)

    override fun getExerciseInstanceDate(exerciseInstanceID: Int?) =
        db.getExerciseInstanceDao().getExerciseInstanceDate(exerciseInstanceID)
}