package com.example.lightweight.data.db.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise
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
class ExerciseDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        categoryDao = database.getCategoryDao()
        exerciseDao = database.getExerciseDao()
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
        exerciseDao.insert(exercise)

        val allExercises = exerciseDao.getAllExercises().getOrAwaitValue()

        assertThat(allExercises).contains(exercise)
    }

    @Test
    fun updateNameTest(): Unit = runBlocking {
        val category = Category("Full body")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Kickers", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val newName = "Twisters"
        exerciseDao.updateName(exercise.exerciseID, newName)

        val allExercises = exerciseDao.getAllExercises().getOrAwaitValue()

        assertThat(allExercises[0].exerciseName).isEqualTo(newName)
    }

    @Test
    fun deleteTest() = runBlocking {
        val category = Category("Neck")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Kickers", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        exerciseDao.delete(exercise)

        val allExercises = exerciseDao.getAllExercises().getOrAwaitValue()

        assertThat(allExercises).doesNotContain(exercise)
    }
}