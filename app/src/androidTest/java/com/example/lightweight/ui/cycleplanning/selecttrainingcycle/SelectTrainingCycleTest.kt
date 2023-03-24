package com.example.lightweight.ui.cycleplanning.selecttrainingcycle

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SelectTrainingCycleTest {
    @Test
    fun testSelectTrainingCycleInView() {
        launchFragmentInContainer<SelectTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.constraint_layout_select_training_cycle)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddCycle_cycleAddedToRecyclerView() {

    }

    @Test
    fun testEditCycle_cycleEditedInRecyclerView() {

    }

    @Test
    fun testDeleteCycle_cycleRemovedFromRecyclerView() {

    }
}