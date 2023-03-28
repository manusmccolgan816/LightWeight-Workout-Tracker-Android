package com.example.lightweight.ui.workouttracking.settracker.exercisehistory

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import com.example.lightweight.data.repositories.FakeExerciseInstanceRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExerciseHistoryFragmentTest {
    @Test
    fun testExerciseHistoryFragmentInView() {
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel
        )
        launchFragmentInContainer<ExerciseHistoryFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_exercise_history)).check(matches(isDisplayed()))
    }
}