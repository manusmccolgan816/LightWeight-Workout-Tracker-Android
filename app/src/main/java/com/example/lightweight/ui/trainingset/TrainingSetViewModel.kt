package com.example.lightweight.ui.trainingset

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.repositories.TrainingSetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingSetViewModel(private val repository: TrainingSetRepository) : ViewModel() {

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

    fun getAllTrainingSets() = repository.getAllTrainingSets()

    fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int) =
         repository.getTrainingSetDatesOfExerciseIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int) =
        repository.getTrainingSetsOfExerciseAndIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        repository.getTrainingSetsOfExerciseInstance(exerciseInstanceID)

    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int) =
        repository.getTrainingSetsOfExerciseRepsIsPR(exerciseID, reps, isPR)

    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int) =
        repository.getTrainingSetsOfExerciseFewerReps(exerciseID, reps)
}