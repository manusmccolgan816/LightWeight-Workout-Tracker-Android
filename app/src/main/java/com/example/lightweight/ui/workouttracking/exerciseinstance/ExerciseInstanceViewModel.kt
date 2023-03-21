package com.example.lightweight.ui.workouttracking.exerciseinstance

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.repositories.ExerciseInstanceRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseInstanceViewModel(private val repository: ExerciseInstanceRepositoryInterface) : ViewModel() {

    fun insert(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(exerciseInstance)
    }

    fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            repository.updateExerciseInstanceNumber(exerciseInstanceID, eiNumber)
        }

    fun decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID: Int?, eiNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            repository.decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID, eiNumber)
        }

    fun delete(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(exerciseInstance)
    }

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

    fun getExerciseInstanceOfID(exerciseInstanceID: Int?) =
        repository.getExerciseInstanceOfID(exerciseInstanceID)

    fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?) =
        repository.getExerciseInstanceOfIDObs(exerciseInstanceID)

    fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?) =
        repository.getExerciseOfExerciseInstance(exerciseInstanceID)

    fun getExerciseInstanceDate(exerciseInstanceID: Int?) =
        repository.getExerciseInstanceDate(exerciseInstanceID)
}