package com.example.lightweight.ui.workouttracking.settracker.exercisehistory

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil.recyclerViewSizeMatcher
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.data.repositories.FakeExerciseInstanceRepository
import com.example.lightweight.data.repositories.FakeTrainingSetRepository
import com.example.lightweight.data.repositories.FakeWorkoutRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExerciseHistoryFragmentTest {
    @Test
    fun testExerciseHistoryFragmentInView() {
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<ExerciseHistoryFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_exercise_history)).check(matches(isDisplayed()))
    }

    @Test
    fun testTrainingSetsDisplayed() {
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val exercise = Exercise("Test Ex", 1)
        exercise.exerciseID = 1
        val workout = Workout("2022-12-03", null)
        workout.workoutID = 1
        val exerciseInstance = ExerciseInstance(
            workout.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance.exerciseInstanceID = 1
        val trainingSet1 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            1,
            100f,
            6,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            2,
            90f,
            8,
            null,
            true
        )


        runBlocking {
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeWorkoutRepository.insert(workout)
            fakeExerciseInstanceRepository.insert(exerciseInstance)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<ExerciseHistoryFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.recycler_view_exercise_instances))
            .check(matches(recyclerViewSizeMatcher(1)))

        // Date is not being checked because the displayed format differs on different devices
        onView(allOf(withText("100.0kg"))).check(matches(isDisplayed()))
        onView(allOf(withText("90.0kg"))).check(matches(isDisplayed()))
        onView(allOf(withText("6 reps"))).check(matches(isDisplayed()))
        onView(allOf(withText("8 reps"))).check(matches(isDisplayed()))
    }
}