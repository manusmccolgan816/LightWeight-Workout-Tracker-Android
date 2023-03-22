package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.lightweight.R
import com.google.common.truth.Truth
import org.junit.Test

class LogSetsFragmentTest {

//    private val kodein = Kodein.lazy {
//        import(testModule, allowOverride = true)
//    }
//
//    private val factory by kodein.instance<TrainingSetViewModelFactory>()
//    private val trainingSetViewModel = factory.create(TrainingSetViewModel::class.java)

    @Test
    fun testSelectLogSetsFragmentInView() {
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        val scenario = launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args
        )

        onView(ViewMatchers.withId(R.id.constraint_layout_log_sets))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testClickSaveWithValidWeightAndReps_trainingSetAddedToRecyclerView() {
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        val scenario = launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args
        )

        onView(withId(R.id.image_view_add_weight)).perform(click())
        onView(withId(R.id.image_view_add_reps)).perform(click())
        onView(withId(R.id.button_add_exercises)).perform(click())
    }

    @Test
    fun testClickSaveWithInvalidWeightAndReps_toastDisplayed() {

    }

    @Test
    fun testClickClear_weightAndRepsCleared() {

    }

    @Test
    fun testClickDecrementWeight_weightDecremented() {

    }

    @Test
    fun testClickIncrementWeight_weightIncremented() {

    }

    @Test
    fun testClickDecrementReps_repsDecremented() {

    }

    @Test
    fun testClickIncrementReps_repsIncremented() {

    }

    @Test
    fun testSavePrTrainingSet_trophyDisplayedBesideRecyclerViewItem() {

    }
}