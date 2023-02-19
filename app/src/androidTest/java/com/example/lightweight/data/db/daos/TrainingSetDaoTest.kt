package com.example.lightweight.data.db.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.*
import com.example.lightweight.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TrainingSetDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var trainingSetDao: TrainingSetDao
    private lateinit var exerciseInstanceDao: ExerciseInstanceDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var workoutDao: WorkoutDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        trainingSetDao = database.getTrainingSetDao()
        exerciseInstanceDao = database.getExerciseInstanceDao()
        exerciseDao = database.getExerciseDao()
        categoryDao = database.getCategoryDao()
        workoutDao = database.getWorkoutDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTest(): Unit = runBlocking {
        val category = Category("Full body")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Kickers", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val workout = Workout("20/10/2000", "Easy")
        workout.workoutID = 1
        workoutDao.insert(workout)
        val exerciseInstance = ExerciseInstance(workout.workoutID, exercise.exerciseID, 1, null)
        exerciseInstance.exerciseInstanceID = 1
        exerciseInstanceDao.insert(exerciseInstance)
        val trainingSet = TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)

        val allTrainingSets = trainingSetDao.getAllTrainingSets().getOrAwaitValue()

        assertThat(allTrainingSets).contains(trainingSet)
    }

    @Test
    fun deleteTest() = runBlocking {

    }
}