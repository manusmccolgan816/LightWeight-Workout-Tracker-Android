package com.example.lightweight.data.db.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Cycle
import com.example.lightweight.data.db.entities.CycleDay
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
class CycleDayDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var cycleDayDao: CycleDayDao
    private lateinit var cycleDao: CycleDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        cycleDayDao = database.getCycleDayDao()
        cycleDao = database.getCycleDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTest(): Unit = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDayDao.insert(cycleDay)

        val allCycleDays = cycleDayDao.getAllCycleDays().getOrAwaitValue()

        assertThat(allCycleDays).contains(cycleDay)
    }

    @Test
    fun updateTest(): Unit = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDay.cycleDayID = 1
        cycleDayDao.insert(cycleDay)
        val newCycleDay = CycleDay(cycle.cycleID, "Full Body 1", 1)
        newCycleDay.cycleDayID = cycleDay.cycleDayID
        cycleDayDao.update(newCycleDay)

        val allCycleDays = cycleDayDao.getAllCycleDays().getOrAwaitValue()

        assertThat(allCycleDays[0].cycleDayName).isEqualTo(newCycleDay.cycleDayName)
    }

    @Test
    fun decrementCycleDayNumbersAfterTest() = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay1 = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDay1.cycleDayID = 1
        cycleDayDao.insert(cycleDay1)
        val cycleDay2 = CycleDay(cycle.cycleID, "FB 2", 2)
        cycleDay2.cycleDayID = 2
        cycleDayDao.insert(cycleDay2)
        val cycleDay3 = CycleDay(cycle.cycleID, "FB 3", 3)
        cycleDay3.cycleDayID = 3
        cycleDayDao.insert(cycleDay3)
        cycleDayDao.decrementCycleDayNumbersAfter(cycle.cycleID, 1)

        val allCycleDays = cycleDayDao.getAllCycleDays().getOrAwaitValue()

        for (cd in allCycleDays) {
            if (cd.cycleDayID == cycleDay2.cycleDayID) {
                assertThat(cd.cycleDayNumber).isEqualTo(1)
            } else if (cd.cycleDayID == cycleDay3.cycleDayID) {
                assertThat(cd.cycleDayNumber).isEqualTo(2)
            }
        }
    }

    @Test
    fun deleteTest() = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDay.cycleDayID = 1
        cycleDayDao.insert(cycleDay)
        cycleDayDao.delete(cycleDay)

        val allCycleDays = cycleDayDao.getAllCycleDays().getOrAwaitValue()

        assertThat(allCycleDays).doesNotContain(cycleDay)
    }
}