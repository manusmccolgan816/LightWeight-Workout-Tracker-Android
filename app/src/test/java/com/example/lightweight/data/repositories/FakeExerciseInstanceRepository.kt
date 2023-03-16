package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.IdNamePair
import com.example.lightweight.data.db.entities.ExerciseInstance

class FakeExerciseInstanceRepository : ExerciseInstanceRepositoryInterface {

    private val exerciseInstances = mutableListOf<ExerciseInstance>()
    private val observableExerciseInstances =
        MutableLiveData<List<ExerciseInstance>>(exerciseInstances)

    private fun refreshLiveData() {
        observableExerciseInstances.postValue(exerciseInstances)
    }

    override suspend fun insert(exerciseInstance: ExerciseInstance) {
        exerciseInstances.add(exerciseInstance)
        refreshLiveData()
    }

    override suspend fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) {
        for (exerciseInstance in exerciseInstances) {
            if (exerciseInstance.exerciseInstanceID == exerciseInstanceID) {
                exerciseInstance.exerciseInstanceNumber = eiNumber
                refreshLiveData()
                return
            }
        }
    }

    override suspend fun decrementExerciseInstanceNumbersOfWorkoutAfter(
        workoutID: Int?,
        eiNumber: Int
    ) {
        for (exerciseInstance in exerciseInstances.filter {
            it.workoutID == workoutID && it.exerciseInstanceNumber > eiNumber
        }) {
            exerciseInstance.exerciseInstanceNumber--
            refreshLiveData()
        }
    }

    override suspend fun delete(exerciseInstance: ExerciseInstance) {
        exerciseInstances.remove(exerciseInstance)
        refreshLiveData()
    }

    override fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?): List<ExerciseInstance> {
        return exerciseInstances.filter {
            it.workoutID == workoutID
        }.sortedBy { it.exerciseInstanceNumber }
    }

    override fun getExerciseInstancesOfWorkout(workoutID: Int?): LiveData<List<ExerciseInstance>> {
        return MutableLiveData(
            exerciseInstances.filter {
                it.workoutID == workoutID
            }.sortedBy { it.exerciseInstanceNumber }
        )
    }

    override fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?): LiveData<List<IdNamePair>> {
        TODO("Joins are going to be a pain")
    }

    override fun getExerciseInstance(workoutID: Int?, exerciseID: Int?): ExerciseInstance? {
        return exerciseInstances.filter {
            it.workoutID == workoutID && it.exerciseID == exerciseID
        }[0]
    }

    override fun getExerciseInstancesOfExercise(exerciseID: Int?): LiveData<List<ExerciseInstance>> {
        return MutableLiveData(exerciseInstances.filter {
            it.exerciseID == exerciseID
        })
    }

    override fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?): LiveData<Map<Int?, String>> {
        TODO("Joins are going to be a pain")
    }

    override fun getExerciseInstanceOfID(exerciseInstanceID: Int?): ExerciseInstance {
        return exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }[0]
    }

    override fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?): LiveData<ExerciseInstance> {
        return MutableLiveData(exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }[0])
    }

    override fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?): LiveData<Int?> {
        return MutableLiveData(exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }[0].exerciseID)
    }

    override fun getExerciseInstanceDate(exerciseInstanceID: Int?): LiveData<String> {
        TODO("Joins are going to be a pain")
    }
}