package com.example.lightweight.data.db.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.lightweight.data.db.WorkoutDatabase
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
class WorkoutDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var dao: WorkoutDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getWorkoutDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTest(): Unit = runBlocking {
        val workout = Workout("07/10/2078", null)
        dao.insert(workout)

        val allWorkouts = dao.getAllWorkouts().getOrAwaitValue()

        assertThat(allWorkouts).contains(workout)
    }

    @Test
    fun deleteTest() = runBlocking {
        val workout = Workout("07/10/2078", null)
        workout.workoutID = 1
        dao.insert(workout)
        dao.delete(workout)

        val allWorkouts = dao.getAllWorkouts().getOrAwaitValue()

        assertThat(allWorkouts).doesNotContain(workout)
    }
}