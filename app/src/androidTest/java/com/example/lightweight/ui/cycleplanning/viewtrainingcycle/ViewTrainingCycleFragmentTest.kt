package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith
import com.example.lightweight.R

@RunWith(AndroidJUnit4ClassRunner::class)
class ViewTrainingCycleFragmentTest {
    @Test
    fun testViewTrainingCycleFragmentInView() {
        val args = bundleOf("cycleID" to 0)
        launchFragmentInContainer<ViewTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args
        )

        onView(withId(R.id.constraint_layout_view_training_cycle)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddCycleDay_cycleDayAddedToRecyclerView() {

    }

    @Test
    fun testAddCycleDayCategory_cycleDayCategoryAddedToRecyclerView() {

    }

    @Test
    fun testAddCycleDayExercise_cycleDayExerciseAddedToRecyclerView() {

    }

    @Test
    fun testEditCycleDay_cycleDayEditedInRecyclerView() {

    }

    @Test
    fun testRemoveCycleDay_cycleDayAndChildrenRemovedFromRecyclerView() {

    }

    @Test
    fun testRemoveCycleDayCategory_cycleDayCategoryAndChildrenRemovedFromRecyclerView() {

    }

    @Test
    fun testRemoveCycleDayExercise_cycleDayExerciseRemovedFromRecyclerView() {

    }
}