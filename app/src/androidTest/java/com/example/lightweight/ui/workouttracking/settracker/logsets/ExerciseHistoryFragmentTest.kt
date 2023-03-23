package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import com.example.lightweight.ui.workouttracking.settracker.exercisehistory.ExerciseHistoryFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExerciseHistoryFragmentTest {
    @Test
    fun testExerciseHistoryFragmentInView() {
        val args = bundleOf("exerciseID" to 1)
        val scenario = launchFragmentInContainer<ExerciseHistoryFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args
        )

        onView(withId(R.id.constraint_layout_exercise_history)).check(matches(isDisplayed()))
    }
}