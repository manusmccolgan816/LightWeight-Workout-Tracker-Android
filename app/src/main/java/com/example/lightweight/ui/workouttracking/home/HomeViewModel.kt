package com.example.lightweight.ui.workouttracking.home

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.repositories.IExerciseInstanceRepository
import com.example.lightweight.data.repositories.ITrainingSetRepository
import com.example.lightweight.data.repositories.IWorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val workoutRepository: IWorkoutRepository,
    private val exerciseInstanceRepository: IExerciseInstanceRepository,
    private val trainingSetRepository: ITrainingSetRepository
) : ViewModel() {

    fun getWorkoutOfDate(date: String) = workoutRepository.getWorkoutOfDate(date)

    fun deleteWorkoutOfID(workoutID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        workoutRepository.deleteWorkoutOfID(workoutID)
    }


    fun deleteExerciseInstance(exerciseInstance: ExerciseInstance) = CoroutineScope(Dispatchers.Main).launch {
        exerciseInstanceRepository.delete(exerciseInstance)
    }

    fun decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID: Int?, eiNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseInstanceRepository.decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID, eiNumber)
        }

    fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?) =
        exerciseInstanceRepository.getExerciseInstancesAndNamesOfWorkout(workoutID)

    fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseInstanceRepository.updateExerciseInstanceNumber(exerciseInstanceID, eiNumber)
        }

    fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?) =
        exerciseInstanceRepository.getExerciseInstanceOfIDObs(exerciseInstanceID)

    fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?) =
        exerciseInstanceRepository.getExerciseOfExerciseInstance(exerciseInstanceID)


    fun deleteTrainingSet(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.delete(trainingSet)
    }

    fun updateIsPR(trainingSetID: Int?, isPR: Int) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.updateIsPR(trainingSetID, isPR)
    }

    fun decrementTrainingSetNumbersAbove(exerciseInstanceID: Int?, trainingSetNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            trainingSetRepository.decrementTrainingSetNumbersAbove(exerciseInstanceID, trainingSetNumber)
        }

    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        trainingSetRepository.getTrainingSetsOfExerciseInstance(exerciseInstanceID)

    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int) =
        trainingSetRepository.getTrainingSetsOfExerciseFewerReps(exerciseID, reps)

    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int) =
        trainingSetRepository.getTrainingSetsOfExerciseAndIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int) =
        trainingSetRepository.getTrainingSetsOfExerciseRepsIsPR(exerciseID, reps, isPR)
}