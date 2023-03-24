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
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExerciseInsightsFragmentTest {
    @Test
    fun testExerciseInsightsFragmentInView() {
        val args = bundleOf("exerciseID" to 1)
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.constraint_layout_exercise_insights)).check(matches(isDisplayed()))
    }
}