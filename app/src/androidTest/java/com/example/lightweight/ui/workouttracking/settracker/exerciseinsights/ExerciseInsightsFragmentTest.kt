package com.example.lightweight.ui.workouttracking.settracker.exerciseinsights

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import com.example.lightweight.R
import com.example.lightweight.data.repositories.FakeExerciseInstanceRepository
import com.example.lightweight.data.repositories.FakeTrainingSetRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExerciseInsightsFragmentTest {
    @Test
    fun testExerciseInsightsFragmentInView() {
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_exercise_insights)).check(matches(isDisplayed()))
    }
}