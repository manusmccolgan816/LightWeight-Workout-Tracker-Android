package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.IdNamePair
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.Workout
import java.lang.Exception

class FakeExerciseInstanceRepository : ExerciseInstanceRepositoryInterface {

    var exercises = mutableListOf<Exercise>()
    var workouts = mutableListOf<Workout>()

    private val exerciseInstances = mutableListOf<ExerciseInstance>()
    val observableExerciseInstances =
        MutableLiveData<List<ExerciseInstance>>(exerciseInstances)

    private var lastId = 0

    private fun refreshLiveData() {
        observableExerciseInstances.postValue(exerciseInstances)
    }

    override suspend fun insert(exerciseInstance: ExerciseInstance) {
        if (exerciseInstance.exerciseInstanceID == null) {
            exerciseInstance.exerciseInstanceID = ++lastId
        }
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
        val idNamesToReturn = mutableListOf<IdNamePair>()

        for (exerciseInstance in exerciseInstances.filter {
            it.workoutID == workoutID
        }.sortedBy { it.exerciseInstanceNumber }) {
            for (exercise in exercises.filter { it.exerciseID == exerciseInstance.exerciseID }) {
                idNamesToReturn.add(
                    IdNamePair(exerciseInstance.exerciseInstanceID, exercise.exerciseName)
                )
            }
        }

        return MutableLiveData(idNamesToReturn)
    }

    override fun getExerciseInstance(workoutID: Int?, exerciseID: Int?): ExerciseInstance? {
        for (exerciseInstance in exerciseInstances) {
            if (exerciseInstance.workoutID == workoutID && exerciseInstance.exerciseID == exerciseID) {
                return exerciseInstance
            }
        }

        return null
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
        for (exerciseInstance in exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }) {
            for (workout in workouts.filter { it.workoutID == exerciseInstance.workoutID }) {
                return MutableLiveData(workout.date)
            }
        }

        throw Exception("exerciseInstance date not found")
    }
}