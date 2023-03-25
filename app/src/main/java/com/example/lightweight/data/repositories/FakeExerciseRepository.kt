package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Exercise
import java.lang.Exception

class FakeExerciseRepository : ExerciseRepositoryInterface {

    private val exercises = mutableListOf<Exercise>()
    private val observableExercises = MutableLiveData<List<Exercise>>(exercises)

    private var lastId = 0

    private fun refreshLiveData() {
        observableExercises.postValue(exercises)
    }

    override suspend fun insert(exercise: Exercise) {
        if (exercise.exerciseID == null) {
            exercise.exerciseID = ++lastId
        }
        exercises.add(exercise)
        refreshLiveData()
    }

    override suspend fun updateName(exerciseID: Int?, exerciseName: String) {
        for (exercise in exercises) {
            if (exercise.exerciseID == exerciseID) {
                exercise.exerciseName = exerciseName
                refreshLiveData()
                return
            }
        }
    }

    override suspend fun delete(exercise: Exercise) {
        exercises.remove(exercise)
        refreshLiveData()
    }

    override fun getExerciseOfID(exerciseID: Int?): Exercise {
        for (exercise in exercises) {
            if (exercise.exerciseID == exerciseID) {
                return exercise
            }
        }
        throw Exception("No exercise of ID")
    }

    override fun getExercisesOfCategory(categoryID: Int?): LiveData<List<Exercise>> {
        val exercisesOfCat = mutableListOf<Exercise>()

        for (exercise in exercises) {
            if (exercise.categoryID == categoryID) {
                exercisesOfCat.add(exercise)
            }
        }
        exercisesOfCat.sortBy { it.exerciseName }

        return MutableLiveData(exercisesOfCat)
    }
}