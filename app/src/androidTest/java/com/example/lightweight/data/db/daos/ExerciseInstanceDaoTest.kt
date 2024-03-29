package com.example.lightweight.data.db.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.Workout
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
class ExerciseInstanceDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
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
        exerciseInstanceDao.insert(exerciseInstance)

        val allExerciseInstances = exerciseInstanceDao.getAllExerciseInstances().getOrAwaitValue()

        assertThat(allExerciseInstances).contains(exerciseInstance)
    }

    @Test
    fun updateExerciseInstanceNumberTest() = runBlocking {
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
        val newEiNumber = 2
        exerciseInstanceDao.updateExerciseInstanceNumber(
            exerciseInstance.exerciseInstanceID,
            newEiNumber
        )

        val allExerciseInstances = exerciseInstanceDao.getAllExerciseInstances().getOrAwaitValue()

        assertThat(allExerciseInstances[0].exerciseInstanceNumber).isEqualTo(newEiNumber)
    }

    @Test
    fun decrementExerciseInstanceNumbersAfterTest() = runBlocking {
        val category = Category("Full body")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Kickers", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val workout = Workout("20/10/2000", "Easy")
        workout.workoutID = 1
        workoutDao.insert(workout)
        val exerciseInstance1 = ExerciseInstance(workout.workoutID, exercise.exerciseID, 1, null)
        exerciseInstance1.exerciseInstanceID = 1
        exerciseInstanceDao.insert(exerciseInstance1)
        val exerciseInstance2 = ExerciseInstance(workout.workoutID, exercise.exerciseID, 2, null)
        exerciseInstance2.exerciseInstanceID = 2
        exerciseInstanceDao.insert(exerciseInstance2)
        val exerciseInstance3 = ExerciseInstance(workout.workoutID, exercise.exerciseID, 3, null)
        exerciseInstance3.exerciseInstanceID = 3
        exerciseInstanceDao.insert(exerciseInstance3)
        exerciseInstanceDao.decrementExerciseInstanceNumbersOfWorkoutAfter(workout.workoutID, 1)

        val dbExerciseInstance2 =
            exerciseInstanceDao.getExerciseInstanceOfID(exerciseInstance2.exerciseInstanceID)
        val dbExerciseInstance3 =
            exerciseInstanceDao.getExerciseInstanceOfID(exerciseInstance3.exerciseInstanceID)

        assertThat(dbExerciseInstance2.exerciseInstanceNumber).isEqualTo(1)
        assertThat(dbExerciseInstance3.exerciseInstanceNumber).isEqualTo(2)
    }

    @Test
    fun deleteTest() = runBlocking {
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
        exerciseInstanceDao.delete(exerciseInstance)

        val allExerciseInstances = exerciseInstanceDao.getAllExerciseInstances().getOrAwaitValue()

        assertThat(allExerciseInstances).doesNotContain(exerciseInstance)
    }
}