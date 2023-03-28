package com.example.lightweight.ui.cycleplanning.selecttrainingcycle

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil
import com.example.lightweight.AndroidTestUtil.clickChildViewWithId
import com.example.lightweight.R
import com.example.lightweight.data.repositories.FakeCycleRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SelectTrainingCycleFragmentTest {
    @Test
    fun testSelectTrainingCycleInView() {
        val testCycleViewModel = CycleViewModel(FakeCycleRepository())

        val factory = LightweightFragmentFactory(
            cycleViewModel = testCycleViewModel
        )
        launchFragmentInContainer<SelectTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_select_training_cycle)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddCycle_cycleAddedToRecyclerView() {
        val fakeCycleRepository = FakeCycleRepository()

        val testCycleViewModel = CycleViewModel(fakeCycleRepository)

        val factory = LightweightFragmentFactory(
            cycleViewModel = testCycleViewModel
        )
        launchFragmentInContainer<SelectTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.extended_fab_add_training_cycle)).perform(click())
        onView(withId(R.id.edit_text_new_training_cycle_name)).perform(replaceText("Test Cycle"))
        onView(withId(R.id.button_save_new_training_cycle)).perform(click())

        onView(withId(R.id.recycler_view_training_cycles))
            .perform(
                actionOnItemAtPosition<TrainingCycleItemAdapter.TrainingCycleItemViewHolderNoDesc>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("Test Cycle")))
            )
    }

    @Test
    fun testEditCycle_cycleEditedInRecyclerView() {
        val fakeCycleRepository = FakeCycleRepository()

        val testCycleViewModel = CycleViewModel(fakeCycleRepository)

        val factory = LightweightFragmentFactory(
            cycleViewModel = testCycleViewModel
        )
        launchFragmentInContainer<SelectTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.extended_fab_add_training_cycle)).perform(click())
        onView(withId(R.id.edit_text_new_training_cycle_name)).perform(replaceText("Test Cycle"))
        onView(withId(R.id.button_save_new_training_cycle)).perform(click())

        onView(withId(R.id.recycler_view_training_cycles))
            .perform(
                actionOnItemAtPosition<TrainingCycleItemAdapter.TrainingCycleItemViewHolderNoDesc>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("Test Cycle")))
            )

        onView(withId(R.id.recycler_view_training_cycles))
            .perform(
                actionOnItemAtPosition<TrainingCycleItemAdapter.TrainingCycleItemViewHolderNoDesc>(
                    0,
                    clickChildViewWithId(R.id.image_view_training_cycle_options)
                )
            )

        onView(withText("Edit")).perform(click())
        onView(withId(R.id.edit_text_edit_training_cycle_name)).perform(replaceText("Edited Cycle"))
        onView(withId(R.id.button_save_edit_training_cycle)).perform(click())

        onView(withId(R.id.recycler_view_training_cycles))
            .perform(
                actionOnItemAtPosition<TrainingCycleItemAdapter.TrainingCycleItemViewHolderNoDesc>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("Edited Cycle")))
            )
    }

    @Test
    fun testDeleteCycle_cycleRemovedFromRecyclerView() {
        val fakeCycleRepository = FakeCycleRepository()

        val testCycleViewModel = CycleViewModel(fakeCycleRepository)

        val factory = LightweightFragmentFactory(
            cycleViewModel = testCycleViewModel
        )
        launchFragmentInContainer<SelectTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.extended_fab_add_training_cycle)).perform(click())
        onView(withId(R.id.edit_text_new_training_cycle_name)).perform(replaceText("Test Cycle"))
        onView(withId(R.id.button_save_new_training_cycle)).perform(click())

        onView(withId(R.id.recycler_view_training_cycles))
            .perform(
                actionOnItemAtPosition<TrainingCycleItemAdapter.TrainingCycleItemViewHolderNoDesc>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("Test Cycle")))
            )

        onView(withId(R.id.recycler_view_training_cycles))
            .perform(
                actionOnItemAtPosition<TrainingCycleItemAdapter.TrainingCycleItemViewHolderNoDesc>(
                    0,
                    clickChildViewWithId(R.id.image_view_training_cycle_options)
                )
            )

        onView(withText("Delete")).perform(click())
        onView(withId(R.id.button_confirm_delete_training_cycle)).perform(click())

        // Assert that the recycler view is empty
        onView(withId(R.id.recycler_view_training_cycles)).check(
            matches(
                AndroidTestUtil.recyclerViewSizeMatcher(
                    0
                )
            )
        )
    }
}