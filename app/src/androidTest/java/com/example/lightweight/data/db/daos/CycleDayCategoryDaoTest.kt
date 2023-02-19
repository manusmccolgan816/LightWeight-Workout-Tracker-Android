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
class CycleDayCategoryDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var cycleDayDao: CycleDayDao
    private lateinit var cycleDao: CycleDao
    private lateinit var cycleDayCategoryDao: CycleDayCategoryDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        cycleDayDao = database.getCycleDayDao()
        cycleDao = database.getCycleDao()
        cycleDayCategoryDao = database.getCycleDayCategoryDao()
        categoryDao = database.getCategoryDao()
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
        cycleDay.cycleDayID = 1
        cycleDayDao.insert(cycleDay)
        val category = Category("Biceps")
        category.categoryID = 1
        categoryDao.insert(category)
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        cycleDayCategoryDao.insert(cycleDayCategory)

        val allCycleDayCategories = cycleDayCategoryDao.getAllCycleDayCategories().getOrAwaitValue()

        assertThat(allCycleDayCategories).contains(cycleDayCategory)
    }

    @Test
    fun deleteTest() = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDay.cycleDayID = 1
        cycleDayDao.insert(cycleDay)
        val category = Category("Biceps")
        category.categoryID = 1
        categoryDao.insert(category)
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        cycleDayCategoryDao.insert(cycleDayCategory)
        cycleDayCategoryDao.delete(cycleDayCategory)

        val allCycleDayCategories = cycleDayCategoryDao.getAllCycleDayCategories().getOrAwaitValue()

        assertThat(allCycleDayCategories).doesNotContain(cycleDayCategory)
    }
}