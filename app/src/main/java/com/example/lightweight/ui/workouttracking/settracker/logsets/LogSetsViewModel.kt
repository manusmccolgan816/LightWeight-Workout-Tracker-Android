package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.data.repositories.IExerciseInstanceRepository
import com.example.lightweight.data.repositories.IExerciseRepository
import com.example.lightweight.data.repositories.ITrainingSetRepository
import com.example.lightweight.data.repositories.IWorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogSetsViewModel(
    private val exerciseRepository: IExerciseRepository,
    private val workoutRepository: IWorkoutRepository,
    private val exerciseInstanceRepository: IExerciseInstanceRepository,
    private val trainingSetRepository: ITrainingSetRepository
) : ViewModel() {

    // Exercise methods

    fun getExerciseOfID(exerciseID: Int?) = exerciseRepository.getExerciseOfID(exerciseID)

    // Workout methods

    fun insertWorkout(workout: Workout) = CoroutineScope(Dispatchers.Main).launch {
        workoutRepository.insert(workout)
    }

    fun deleteWorkout(workout: Workout) = CoroutineScope(Dispatchers.Main).launch {
        workoutRepository.delete(workout)
    }

    fun deleteWorkoutOfID(workoutID: Int?) = CoroutineScope(Dispatchers.Main).launch {
        workoutRepository.deleteWorkoutOfID(workoutID)
    }

    fun getWorkoutOfDate(date: String) = workoutRepository.getWorkoutOfDate(date)

    fun getWorkoutDates() = workoutRepository.getWorkoutDates()

    // ExerciseInstance methods

    fun insertExerciseInstance(exerciseInstance: ExerciseInstance) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseInstanceRepository.insert(exerciseInstance)
        }

    fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseInstanceRepository.updateExerciseInstanceNumber(exerciseInstanceID, eiNumber)
        }

    fun decrementExerciseInstanceNumbersOfWorkoutAfter(workoutID: Int?, eiNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseInstanceRepository.decrementExerciseInstanceNumbersOfWorkoutAfter(
                workoutID,
                eiNumber
            )
        }

    fun deleteExerciseInstance(exerciseInstance: ExerciseInstance) =
        CoroutineScope(Dispatchers.Main).launch {
            exerciseInstanceRepository.delete(exerciseInstance)
        }

    fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?) =
        exerciseInstanceRepository.getExerciseInstancesOfWorkoutNoLiveData(workoutID)

    fun getExerciseInstancesOfWorkout(workoutID: Int?) =
        exerciseInstanceRepository.getExerciseInstancesOfWorkout(workoutID)

    fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?) =
        exerciseInstanceRepository.getExerciseInstancesAndNamesOfWorkout(workoutID)

    fun getExerciseInstance(workoutID: Int?, exerciseID: Int?) =
        exerciseInstanceRepository.getExerciseInstance(workoutID, exerciseID)

    fun getExerciseInstancesOfExercise(exerciseID: Int?) =
        exerciseInstanceRepository.getExerciseInstancesOfExercise(exerciseID)

    fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?) =
        exerciseInstanceRepository.getExerciseInstancesAndDatesOfExercise(exerciseID)

    fun getExerciseInstanceOfID(exerciseInstanceID: Int?) =
        exerciseInstanceRepository.getExerciseInstanceOfID(exerciseInstanceID)

    fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?) =
        exerciseInstanceRepository.getExerciseInstanceOfIDObs(exerciseInstanceID)

    fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?) =
        exerciseInstanceRepository.getExerciseOfExerciseInstance(exerciseInstanceID)

    fun getExerciseInstanceDate(exerciseInstanceID: Int?) =
        exerciseInstanceRepository.getExerciseInstanceDate(exerciseInstanceID)

    // TrainingSet methods

    fun insertTrainingSet(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.insert(trainingSet)
    }

    fun deleteTrainingSet(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.delete(trainingSet)
    }

    fun updateTrainingSet(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.update(trainingSet)
    }

    fun updateTrainingSetIsPR(trainingSetID: Int?, isPR: Int) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.updateIsPR(trainingSetID, isPR)
    }

    fun updateTrainingSetNote(trainingSetID: Int?, note: String?) = CoroutineScope(Dispatchers.Main).launch {
        trainingSetRepository.updateNote(trainingSetID, note)
    }

    fun decrementTrainingSetNumbersAbove(exerciseInstanceID: Int?, trainingSetNumber: Int) =
        CoroutineScope(Dispatchers.Main).launch {
            trainingSetRepository.decrementTrainingSetNumbersAbove(exerciseInstanceID, trainingSetNumber)
        }

    fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int) =
        trainingSetRepository.getTrainingSetDatesOfExerciseIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int) =
        trainingSetRepository.getTrainingSetsOfExerciseAndIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        trainingSetRepository.getTrainingSetsOfExerciseInstance(exerciseInstanceID)

    fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?) =
        trainingSetRepository.getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID)

    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int) =
        trainingSetRepository.getTrainingSetsOfExerciseRepsIsPR(exerciseID, reps, isPR)

    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int) =
        trainingSetRepository.getTrainingSetsOfExerciseFewerReps(exerciseID, reps)

    fun getTrainingSetsOfExercise(exerciseID: Int?) =
        trainingSetRepository.getTrainingSetsOfExercise(exerciseID)
}