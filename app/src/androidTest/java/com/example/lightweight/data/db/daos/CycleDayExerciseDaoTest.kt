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
class CycleDayExerciseDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var cycleDayDao: CycleDayDao
    private lateinit var cycleDao: CycleDao
    private lateinit var cycleDayCategoryDao: CycleDayCategoryDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var cycleDayExerciseDao: CycleDayExerciseDao

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
        exerciseDao = database.getExerciseDao()
        cycleDayExerciseDao = database.getCycleDayExerciseDao()
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
        val exercise = Exercise("Curls", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        cycleDayCategoryDao.insert(cycleDayCategory)
        val cycleDayExercise = CycleDayExercise(
            cycleDay.cycleDayID,
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise.cycleDayExerciseID = 1
        cycleDayExerciseDao.insert(cycleDayExercise)

        val allCycleDayExercises = cycleDayExerciseDao.getAllCycleDayExercises().getOrAwaitValue()

        assertThat(allCycleDayExercises).contains(cycleDayExercise)
    }

    @Test
    fun decrementCycleDayExerciseNumbersAfter() = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDay.cycleDayID = 1
        cycleDayDao.insert(cycleDay)
        val category = Category("Biceps")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Curls", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        cycleDayCategoryDao.insert(cycleDayCategory)
        val cycleDayExercise1 = CycleDayExercise(
            cycleDay.cycleDayID,
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise1.cycleDayExerciseID = 1
        cycleDayExerciseDao.insert(cycleDayExercise1)
        val cycleDayExercise2 = CycleDayExercise(
            cycleDay.cycleDayID,
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            2
        )
        cycleDayExercise2.cycleDayExerciseID = 2
        cycleDayExerciseDao.insert(cycleDayExercise2)
        val cycleDayExercise3 = CycleDayExercise(
            cycleDay.cycleDayID,
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            3
        )
        cycleDayExercise3.cycleDayExerciseID = 3
        cycleDayExerciseDao.insert(cycleDayExercise3)
        cycleDayExerciseDao.decrementCycleDayExerciseNumbersAfter(
            cycleDayCategory.cycleDayCategoryID,
            1
        )

        val allCycleDayExercises = cycleDayExerciseDao.getAllCycleDayExercises().getOrAwaitValue()

        for (cde in allCycleDayExercises) {
            if (cde.cycleDayExerciseID == cycleDayExercise2.cycleDayExerciseID) {
                assertThat(cde.cycleDayExerciseNumber).isEqualTo(1)
            } else if (cde.cycleDayExerciseID == cycleDayExercise3.cycleDayExerciseID) {
                assertThat(cde.cycleDayExerciseNumber).isEqualTo(2)
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
        val category = Category("Biceps")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Curls", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        cycleDayCategoryDao.insert(cycleDayCategory)
        val cycleDayExercise = CycleDayExercise(
            cycleDay.cycleDayID,
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise.cycleDayExerciseID = 1
        cycleDayExerciseDao.insert(cycleDayExercise)
        cycleDayExerciseDao.delete(cycleDayExercise)

        val allCycleDayExercises = cycleDayExerciseDao.getAllCycleDayExercises().getOrAwaitValue()

        assertThat(allCycleDayExercises).doesNotContain(cycleDayExercise)
    }

    @Test
    fun deleteOfIDTest() = runBlocking {
        val cycle = Cycle("3 Day Full Body", null)
        cycle.cycleID = 1
        cycleDao.insert(cycle)
        val cycleDay = CycleDay(cycle.cycleID, "FB 1", 1)
        cycleDay.cycleDayID = 1
        cycleDayDao.insert(cycleDay)
        val category = Category("Biceps")
        category.categoryID = 1
        categoryDao.insert(category)
        val exercise = Exercise("Curls", category.categoryID)
        exercise.exerciseID = 1
        exerciseDao.insert(exercise)
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        cycleDayCategoryDao.insert(cycleDayCategory)
        val cycleDayExercise = CycleDayExercise(
            cycleDay.cycleDayID,
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise.cycleDayExerciseID = 1
        cycleDayExerciseDao.insert(cycleDayExercise)
        cycleDayExerciseDao.deleteOfID(cycleDayExercise.cycleDayExerciseID)

        val allCycleDayExercises = cycleDayExerciseDao.getAllCycleDayExercises().getOrAwaitValue()

        assertThat(allCycleDayExercises).doesNotContain(cycleDayExercise)
    }
}