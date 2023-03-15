package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.IdNamePair
import com.example.lightweight.data.db.entities.ExerciseInstance

class FakeExerciseInstanceRepository : ExerciseInstanceRepositoryInterface {

    private val exerciseInstances = mutableListOf<ExerciseInstance>()
    private val observableExerciseInstances =
        MutableLiveData<List<ExerciseInstance>>(exerciseInstances)

    override suspend fun insert(exerciseInstance: ExerciseInstance) {
        TODO("Not yet implemented")
    }

    override suspend fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun decrementExerciseInstanceNumbersOfWorkoutAfter(
        workoutID: Int?,
        eiNumber: Int
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(exerciseInstance: ExerciseInstance) {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?): List<ExerciseInstance> {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstancesOfWorkout(workoutID: Int?): LiveData<List<ExerciseInstance>> {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?): LiveData<List<IdNamePair>> {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstance(workoutID: Int?, exerciseID: Int?): ExerciseInstance? {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstancesOfExercise(exerciseID: Int?): LiveData<List<ExerciseInstance>> {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?): LiveData<Map<Int?, String>> {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstanceOfID(exerciseInstanceID: Int?): ExerciseInstance {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?): LiveData<ExerciseInstance> {
        TODO("Not yet implemented")
    }

    override fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?): LiveData<Int?> {
        TODO("Not yet implemented")
    }

    override fun getExerciseInstanceDate(exerciseInstanceID: Int?): LiveData<String> {
        TODO("Not yet implemented")
    }
}