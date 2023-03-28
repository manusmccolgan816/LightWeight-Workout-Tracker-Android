package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.IdNamePair
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.Workout
import java.lang.Exception

class FakeExerciseInstanceRepository : ExerciseInstanceRepositoryInterface {

    private val allTag = 0
    private val exerciseInstancesTag = 1
    private val exerciseInstancesOfWorkoutTag = 2
    private val exerciseInstancesAndNamesOfWorkoutTag = 3
    private val exerciseInstancesOfExerciseTag = 4
    private val exerciseInstancesAndDatesOfExerciseTag = 5
    private val exerciseInstanceOfIDObsTag = 6
    private val exerciseOfExerciseInstanceTag = 7
    private val exerciseInstanceDateTag = 8

    var exercises = mutableListOf<Exercise>()
    var workouts = mutableListOf<Workout>()

    private val exerciseInstances = mutableListOf<ExerciseInstance>()
    val observableExerciseInstances =
        MutableLiveData<List<ExerciseInstance>>(exerciseInstances)

    private val observableExerciseInstancesOfWorkout = MutableLiveData<List<ExerciseInstance>>()
    private var exerciseInstancesOfWorkoutParam: Int? = null

    private val observableExerciseInstancesAndNamesOfWorkout = MutableLiveData<List<IdNamePair>>()
    private var exerciseInstancesAndNamesOfWorkoutParam: Int? = null

    private val observableExerciseInstancesOfExercise = MutableLiveData<List<ExerciseInstance>>()
    private var exerciseInstancesOfExerciseParam: Int? = null

    private val observableExerciseInstancesAndDatesOfExercise = MutableLiveData<Map<Int?, String>>()
    private var exerciseInstancesAndDatesOfExerciseParam: Int? = null

    private val observableExerciseInstanceOfIDObs = MutableLiveData<ExerciseInstance>()
    private var exerciseInstanceOfIDObsParam: Int? = null

    private val observableExerciseOfExerciseInstance = MutableLiveData<Int?>()
    private var exerciseOfExerciseInstanceParam: Int? = null

    private val observableExerciseInstanceDate = MutableLiveData<String>()
    private var exerciseInstanceDateParam: Int? = null

    private var lastId = 0

    private fun refreshLiveData(tag: Int?) {
        if (tag == allTag || tag == exerciseInstancesTag) {
            observableExerciseInstances.postValue(exerciseInstances)
        }
        if (tag == allTag || tag == exerciseInstancesOfWorkoutTag) {
            if (exerciseInstancesOfWorkoutParam != null) {
                observableExerciseInstancesOfWorkout.postValue(
                    calcExerciseInstancesOfWorkout(exerciseInstancesOfWorkoutParam)
                )
            }
        }
        if (tag == allTag || tag == exerciseInstancesAndNamesOfWorkoutTag) {
            if (exerciseInstancesAndNamesOfWorkoutParam != null) {
                observableExerciseInstancesAndNamesOfWorkout.postValue(
                    calcExerciseInstancesAndNamesOfWorkout(exerciseInstancesAndNamesOfWorkoutParam)
                )
            }
        }
        if (tag == allTag || tag == exerciseInstancesOfExerciseTag) {
            if (exerciseInstancesOfExerciseParam != null) {
                observableExerciseInstancesOfExercise.postValue(
                    calcExerciseInstancesOfExercise(exerciseInstancesOfExerciseParam)
                )
            }
        }
        if (tag == allTag || tag == exerciseInstancesAndDatesOfExerciseTag) {
            if (exerciseInstancesAndDatesOfExerciseParam != null) {
                observableExerciseInstancesAndDatesOfExercise.postValue(
                    calcExerciseInstancesAndDatesOfExercise(exerciseInstancesAndDatesOfExerciseTag)
                )
            }
        }
        if (tag == allTag || tag == exerciseInstanceOfIDObsTag) {
            if (exerciseInstanceOfIDObsParam != null) {
                observableExerciseInstanceOfIDObs.postValue(
                    calcExerciseInstanceOfIDObs(exerciseInstanceOfIDObsParam)
                )
            }
        }
        if (tag == allTag || tag == exerciseOfExerciseInstanceTag) {
            if (exerciseOfExerciseInstanceParam != null) {
                observableExerciseOfExerciseInstance.postValue(
                    calcExerciseOfExerciseInstance(exerciseOfExerciseInstanceParam)
                )
            }
        }
        if (tag == allTag || tag == exerciseInstanceDateTag) {
            if (exerciseInstanceDateParam != null) {
                observableExerciseInstanceDate.postValue(
                    calcExerciseInstanceDate(exerciseInstanceDateParam)
                )
            }
        }
    }

    override suspend fun insert(exerciseInstance: ExerciseInstance) {
        if (exerciseInstance.exerciseInstanceID == null) {
            exerciseInstance.exerciseInstanceID = ++lastId
        }
        exerciseInstances.add(exerciseInstance)
        refreshLiveData(allTag)
    }

    override suspend fun updateExerciseInstanceNumber(exerciseInstanceID: Int?, eiNumber: Int) {
        for (exerciseInstance in exerciseInstances) {
            if (exerciseInstance.exerciseInstanceID == exerciseInstanceID) {
                exerciseInstance.exerciseInstanceNumber = eiNumber
                refreshLiveData(allTag)
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
            refreshLiveData(allTag)
        }
    }

    override suspend fun delete(exerciseInstance: ExerciseInstance) {
        exerciseInstances.remove(exerciseInstance)
        refreshLiveData(allTag)
    }

    override fun getExerciseInstancesOfWorkoutNoLiveData(workoutID: Int?): List<ExerciseInstance> {
        return exerciseInstances.filter {
            it.workoutID == workoutID
        }.sortedBy { it.exerciseInstanceNumber }
    }

    override fun getExerciseInstancesOfWorkout(workoutID: Int?): LiveData<List<ExerciseInstance>> {
        exerciseInstancesOfWorkoutParam = workoutID
        refreshLiveData(exerciseInstancesOfWorkoutTag)
        return observableExerciseInstancesOfWorkout
    }

    private fun calcExerciseInstancesOfWorkout(workoutID: Int?): List<ExerciseInstance> {
        return exerciseInstances.filter {
            it.workoutID == workoutID
        }.sortedBy { it.exerciseInstanceNumber }
    }

    override fun getExerciseInstancesAndNamesOfWorkout(workoutID: Int?): LiveData<List<IdNamePair>> {
        exerciseInstancesAndNamesOfWorkoutParam = workoutID
        refreshLiveData(exerciseInstancesAndNamesOfWorkoutTag)
        return observableExerciseInstancesAndNamesOfWorkout
    }

    private fun calcExerciseInstancesAndNamesOfWorkout(workoutID: Int?): List<IdNamePair> {
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

        return idNamesToReturn
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
        exerciseInstancesOfExerciseParam = exerciseID
        refreshLiveData(exerciseInstancesOfExerciseTag)
        return observableExerciseInstancesOfExercise
    }

    private fun calcExerciseInstancesOfExercise(exerciseID: Int?): List<ExerciseInstance> {
        return exerciseInstances.filter {
            it.exerciseID == exerciseID
        }
    }

    override fun getExerciseInstancesAndDatesOfExercise(exerciseID: Int?): LiveData<Map<Int?, String>> {
        exerciseInstancesAndDatesOfExerciseParam = exerciseID
        refreshLiveData(exerciseInstancesAndDatesOfExerciseTag)
        return observableExerciseInstancesAndDatesOfExercise
    }

    private fun calcExerciseInstancesAndDatesOfExercise(exerciseID: Int?): Map<Int?, String> {
        val mapToReturn = mutableMapOf<Int?, String>()

        for (exerciseInstance in exerciseInstances.filter { it.exerciseID == exerciseID }) {
            for (workout in workouts.filter { exerciseInstance.workoutID == it.workoutID }) {
                mapToReturn[exerciseInstance.exerciseInstanceID] = workout.date
            }
        }

        return mapToReturn.toList().sortedBy { (_, value) -> value }.toMap()
    }

    override fun getExerciseInstanceOfID(exerciseInstanceID: Int?): ExerciseInstance {
        return exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }[0]
    }

    override fun getExerciseInstanceOfIDObs(exerciseInstanceID: Int?): LiveData<ExerciseInstance> {
        exerciseInstanceOfIDObsParam = exerciseInstanceID
        refreshLiveData(exerciseInstanceOfIDObsTag)
        return observableExerciseInstanceOfIDObs
    }

    private fun calcExerciseInstanceOfIDObs(exerciseInstanceID: Int?): ExerciseInstance {
        return exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }[0]
    }

    override fun getExerciseOfExerciseInstance(exerciseInstanceID: Int?): LiveData<Int?> {
        exerciseOfExerciseInstanceParam = exerciseInstanceID
        refreshLiveData(exerciseOfExerciseInstanceTag)
        return observableExerciseOfExerciseInstance
    }

    private fun calcExerciseOfExerciseInstance(exerciseInstanceID: Int?): Int? {
        return exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }[0].exerciseID
    }

    override fun getExerciseInstanceDate(exerciseInstanceID: Int?): LiveData<String> {
        exerciseInstanceDateParam = exerciseInstanceID
        refreshLiveData(exerciseInstanceDateTag)
        return observableExerciseInstanceDate
    }

    private fun calcExerciseInstanceDate(exerciseInstanceID: Int?): String {
        for (exerciseInstance in exerciseInstances.filter {
            it.exerciseInstanceID == exerciseInstanceID
        }) {
            for (workout in workouts.filter { it.workoutID == exerciseInstance.workoutID }) {
                return workout.date
            }
        }

        throw Exception("exerciseInstance date not found")
    }
}