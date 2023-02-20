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
        val trainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)

        val allTrainingSets = trainingSetDao.getAllTrainingSets().getOrAwaitValue()

        assertThat(allTrainingSets).isNotEmpty()
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
        val trainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)
        trainingSetDao.delete(trainingSet)

        val allTrainingSets = trainingSetDao.getAllTrainingSets().getOrAwaitValue()

        assertThat(allTrainingSets).isEmpty()
    }

    @Test
    fun updateTest(): Unit = runBlocking {
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
        val trainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)
        val newReps = 12
        val newTrainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, newReps, "easy", false)
        newTrainingSet.trainingSetID = trainingSet.trainingSetID
        trainingSetDao.update(newTrainingSet)

        val allTrainingSets = trainingSetDao.getAllTrainingSets().getOrAwaitValue()

        assertThat(allTrainingSets[0].reps).isEqualTo(newReps)
    }

    @Test
    fun updateIsPRTest(): Unit = runBlocking {
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
        val trainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)
        trainingSetDao.updateIsPR(trainingSet.trainingSetID, 1) // 1 is used instead of true

        val allTrainingSets = trainingSetDao.getAllTrainingSets().getOrAwaitValue()

        assertThat(allTrainingSets[0].isPR).isEqualTo(true)
    }

    @Test
    fun updateIsNoteTest(): Unit = runBlocking {
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
        val trainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)
        val newNote = "sore wrists"
        trainingSetDao.updateNote(trainingSet.trainingSetID, newNote)

        val allTrainingSets = trainingSetDao.getAllTrainingSets().getOrAwaitValue()

        assertThat(allTrainingSets[0].note).isEqualTo(newNote)
    }

    @Test
    fun decrementTrainingSetNumbersAbove(): Unit = runBlocking {
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
        val trainingSet =
            TrainingSet(exerciseInstance.exerciseInstanceID, 1, 40.5f, 10, "easy", false)
        trainingSet.trainingSetID = 1
        trainingSetDao.insert(trainingSet)
        val trainingSet2 =
            TrainingSet(exerciseInstance.exerciseInstanceID, 3, 40.5f, 10, null, false)
        trainingSet.trainingSetID = 2
        trainingSetDao.insert(trainingSet2)
        val trainingSet3 =
            TrainingSet(exerciseInstance.exerciseInstanceID, 4, 35.5f, 11, null, false)
        trainingSet.trainingSetID = 3
        trainingSetDao.insert(trainingSet3)
        trainingSetDao.decrementTrainingSetNumbersAbove(exerciseInstance.exerciseInstanceID, 1)

        val trainingSets =
            trainingSetDao.getTrainingSetsOfExerciseInstance(exerciseInstance.exerciseInstanceID)
                .getOrAwaitValue()

        assertThat(trainingSets[1].trainingSetNumber).isEqualTo(2)
        assertThat(trainingSets[2].trainingSetNumber).isEqualTo(3)
    }
}