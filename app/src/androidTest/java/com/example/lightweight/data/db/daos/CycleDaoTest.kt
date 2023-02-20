package com.example.lightweight.data.db.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Cycle
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
class CycleDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var dao: CycleDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getCycleDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTest(): Unit = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        dao.insert(cycle)

        val allCycles = dao.getAllCycles().getOrAwaitValue()

        assertThat(allCycles).contains(cycle)
    }

    @Test
    fun updateTest(): Unit = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        dao.insert(cycle)
        val newCycle = Cycle("6 Day PPL", null)
        newCycle.cycleID = cycle.cycleID
        dao.update(newCycle)

        val allCycles = dao.getAllCycles().getOrAwaitValue()

        assertThat(allCycles).contains(newCycle)
    }

    @Test
    fun deleteTest() = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        dao.insert(cycle)
        dao.delete(cycle)

        val allCycles = dao.getAllCycles().getOrAwaitValue()

        assertThat(allCycles).doesNotContain(cycle)
    }
}