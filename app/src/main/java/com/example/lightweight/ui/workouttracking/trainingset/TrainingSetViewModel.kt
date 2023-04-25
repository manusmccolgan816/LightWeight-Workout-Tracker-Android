package com.example.lightweight.ui.workouttracking.trainingset

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.repositories.ITrainingSetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingSetViewModel(private val repository: ITrainingSetRepository) : ViewModel() {

    fun insert(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(trainingSet)
    }

    fun delete(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(trainingSet)
    }

    fun update(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(trainingSet)
    }

    fun updateIsPR(trainingSetID: Int?, isPR: Int) = CoroutineScope(Dispatchers.Main).launch {
        repository.updateIsPR(trainingSetID, isPR)
    }

    fun updateNote(trainingSetID: Int?, note: String?) = CoroutineScope(Dispatchers.Main).launch {
        repository.updateNote(trainingSetID, note)
    }

    fun decrementTrainingSetNumbersAbove(exerciseInstanceID: Int?, trainingSetNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            repository.decrementTrainingSetNumbersAbove(exerciseInstanceID, trainingSetNumber)
        }

    fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int) =
        repository.getTrainingSetDatesOfExerciseIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int) =
        repository.getTrainingSetsOfExerciseAndIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        repository.getTrainingSetsOfExerciseInstance(exerciseInstanceID)

    fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?) =
        repository.getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID)

    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int) =
        repository.getTrainingSetsOfExerciseRepsIsPR(exerciseID, reps, isPR)

    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int) =
        repository.getTrainingSetsOfExerciseFewerReps(exerciseID, reps)

    fun getTrainingSetsOfExercise(exerciseID: Int?) =
        repository.getTrainingSetsOfExercise(exerciseID)
}