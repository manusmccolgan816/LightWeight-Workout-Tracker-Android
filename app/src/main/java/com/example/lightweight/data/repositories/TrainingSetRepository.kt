package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.TrainingSet

class TrainingSetRepository(private val db: WorkoutDatabase) {

    suspend fun insert(trainingSet: TrainingSet) = db.getTrainingSetDao().insert(trainingSet)

    suspend fun delete(trainingSet: TrainingSet) = db.getTrainingSetDao().delete(trainingSet)

    suspend fun updateIsPR(trainingSetID: Int?, isPR: Int) =
        db.getTrainingSetDao().updateIsPR(trainingSetID, isPR)

    fun getAllTrainingSets() = db.getTrainingSetDao().getAllTrainingSets()

    fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int) =
        db.getTrainingSetDao().getTrainingSetDatesOfExerciseIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseAndIsPR(exerciseID, isPR)

    fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseInstance(exerciseInstanceID)

    fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseRepsIsPR(exerciseID, reps, isPR)

    fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseFewerReps(exerciseID, reps)
}