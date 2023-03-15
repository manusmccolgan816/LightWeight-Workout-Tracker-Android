package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import com.example.lightweight.IdNamePair
import com.example.lightweight.data.db.entities.ExerciseInstance

interface ExerciseInstanceRepositoryInterface {
    suspend fun insert(exerciseInstance: ExerciseInstance)
    suspend fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int)
    suspend fun decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID: Int?, eiNumber: Int)
    suspend fun delete(exerciseInstance: ExerciseInstance)
    fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?): List<ExerciseInstance>
    fun getExerciseInstancesOfWorkout(workoutID: Int?): LiveData<List<ExerciseInstance>>
    fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?): LiveData<List<IdNamePair>>
    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?): ExerciseInstance?
    fun getExerciseInstancesOfExercise(exerciseID: Int?): LiveData<List<ExerciseInstance>>
    fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?): LiveData<Map<Int?, String>>
    fun getExerciseInstanceOfID(exerciseInstanceID: Int?): ExerciseInstance
    fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?): LiveData<ExerciseInstance>
    fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?): LiveData<Int?>
    fun getExerciseInstanceDate(exerciseInstanceID: Int?): LiveData<String>
}