package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Exercise

class FakeExerciseRepository : ExerciseRepositoryInterface {

    private val exercises = mutableListOf<Exercise>()
    private val observableExercises = MutableLiveData<List<Exercise>>(exercises)

    override suspend fun insert(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun updateName(exerciseID: Int?, exerciseName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override fun getExerciseOfID(exerciseID: Int?): Exercise {
        TODO("Not yet implemented")
    }

    override fun getExercisesOfCategory(categoryID: Int?): LiveData<List<Exercise>> {
        TODO("Not yet implemented")
    }
}