package com.example.lightweight.ui.exerciseinstance

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.repositories.ExerciseInstanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseInstanceViewModel(private val repository: ExerciseInstanceRepository) : ViewModel() {

    fun insert(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(exerciseInstance)
    }

    fun delete(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(exerciseInstance)
    }

    fun getAllExerciseInstances() = repository.getAllExerciseInstances()

    fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?) =
        repository.getExerciseInstancesOfWorkoutNoLiveData(workoutID)

    fun getExerciseInstancesOfWorkout(workoutID: Int?) =
        repository.getExerciseInstancesOfWorkout(workoutID)

    fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?) =
        repository.getExerciseInstancesAndNamesOfWorkout(workoutID)

    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?) =
        repository.getExerciseInstance(workoutID, exerciseID)

    fun getExerciseInstancesOfExercise(exerciseID: Int?) =
        repository.getExerciseInstancesOfExercise(exerciseID)

    fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?) =
        repository.getExerciseInstancesAndDatesOfExercise(exerciseID)
}