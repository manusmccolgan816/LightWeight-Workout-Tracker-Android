package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Exercise

class FakeExerciseRepository : IExerciseRepository {

    private val allTag = 0
    private val exercisesTag = 1
    private val exercisesOfCategoryTag = 2

    var exerciseInstanceRepo: FakeExerciseInstanceRepository? = null
    var cycleDayExerciseRepo: FakeCycleDayExerciseRepository? = null

    private val exercises = mutableListOf<Exercise>()
    private val observableExercises = MutableLiveData<List<Exercise>>(exercises)

    private val observableExercisesOfCategory = MutableLiveData<List<Exercise>>()
    private var exercisesOfCategoryParam: Int? = null

    private var lastId = 0

    private fun refreshLiveData(tag: Int) {
        if (tag == allTag || tag == exercisesTag) observableExercises.postValue(exercises)
        if (tag == allTag || tag == exercisesOfCategoryTag) {
            if (exercisesOfCategoryParam != null) {
                observableExercisesOfCategory.postValue(
                    calcExercisesOfCategory(exercisesOfCategoryParam)
                )
            }
        }
    }

    override suspend fun insert(exercise: Exercise) {
        if (exercise.exerciseID == null) {
            exercise.exerciseID = ++lastId
        }
        exercises.add(exercise)
        refreshLiveData(allTag)

        if (exerciseInstanceRepo != null) {
            exerciseInstanceRepo?.exercises?.add(exercise)
            exerciseInstanceRepo?.refreshLiveData(allTag)
        }
        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.exercises?.add(exercise)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun updateName(exerciseID: Int?, exerciseName: String) {
        // TODO Update other repository data
        for (exercise in exercises) {
            if (exercise.exerciseID == exerciseID) {
                exercise.exerciseName = exerciseName
                refreshLiveData(allTag)
                return
            }
        }
    }

    override suspend fun delete(exercise: Exercise) {
        exercises.remove(exercise)
        refreshLiveData(allTag)

        if (exerciseInstanceRepo != null) {
            exerciseInstanceRepo?.exercises?.remove(exercise)
            exerciseInstanceRepo?.refreshLiveData(allTag)
        }
        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.exercises?.remove(exercise)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
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
        exercisesOfCategoryParam = categoryID
        refreshLiveData(exercisesOfCategoryTag)
        return observableExercisesOfCategory
    }

    private fun calcExercisesOfCategory(categoryID: Int?): List<Exercise> {
        val exercisesOfCat = mutableListOf<Exercise>()

        for (exercise in exercises) {
            if (exercise.categoryID == categoryID) {
                exercisesOfCat.add(exercise)
            }
        }
        exercisesOfCat.sortBy { it.exerciseName }

        return exercisesOfCat
    }
}