package com.example.lightweight.data.repositories

import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.TrainingSet

class TrainingSetRepository(private val db: WorkoutDatabase) : TrainingSetRepositoryInterface {

    override suspend fun insert(trainingSet: TrainingSet) =
        db.getTrainingSetDao().insert(trainingSet)

    override suspend fun delete(trainingSet: TrainingSet) =
        db.getTrainingSetDao().delete(trainingSet)

    override suspend fun update(trainingSet: TrainingSet) =
        db.getTrainingSetDao().update(trainingSet)

    override suspend fun updateIsPR(trainingSetID: Int?, isPR: Int) =
        db.getTrainingSetDao().updateIsPR(trainingSetID, isPR)

    override suspend fun updateNote(trainingSetID: Int?, note: String?) =
        db.getTrainingSetDao().updateNote(trainingSetID, note)

    override suspend fun decrementTrainingSetNumbersAbove(
        exerciseInstanceID: Int?,
        trainingSetNumber: Int
    ) =
        db.getTrainingSetDao()
            .decrementTrainingSetNumbersAbove(exerciseInstanceID, trainingSetNumber)

    override fun getAllTrainingSets() = db.getTrainingSetDao().getAllTrainingSets()

    override fun getTrainingSetDatesOfExerciseIsPR(exerciseID: Int?, isPR: Int) =
        db.getTrainingSetDao().getTrainingSetDatesOfExerciseIsPR(exerciseID, isPR)

    override fun getTrainingSetsOfExerciseAndIsPR(exerciseID: Int?, isPR: Int) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseAndIsPR(exerciseID, isPR)

    override fun getTrainingSetsOfExerciseInstance(exerciseInstanceID: Int?) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseInstance(exerciseInstanceID)

    override fun getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID: Int?) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseInstanceNoLiveData(exerciseInstanceID)

    override fun getTrainingSetsOfExerciseRepsIsPR(exerciseID: Int?, reps: Int, isPR: Int) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseRepsIsPR(exerciseID, reps, isPR)

    override fun getTrainingSetsOfExerciseFewerReps(exerciseID: Int?, reps: Int) =
        db.getTrainingSetDao().getTrainingSetsOfExerciseFewerReps(exerciseID, reps)

    override fun getTrainingSetsOfExercise(exerciseID: Int?) =
        db.getTrainingSetDao().getTrainingSetsOfExercise(exerciseID)
}