package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import com.example.lightweight.testModule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

@RunWith(AndroidJUnit4ClassRunner::class)
class LogSetsFragmentTest {

//    private val testKodein = Kodein {
//        import(testModule)
//    }

//    private val kodein = Kodein.lazy {
//        import(testModule, allowOverride = true)
//    }

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

        onView(withId(R.id.constraint_layout_log_sets))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testClickSaveWithValidWeightAndReps_trainingSetAddedToRecyclerView() {
        // TODO: REQUIRES FAKE REPOSITORY TO BE USED
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        //val fragment: LogSetsFragment = testKodein.direct.instance()
        val scenario = launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.image_view_add_weight)).perform(click())
        onView(withId(R.id.image_view_add_reps)).perform(click())
        onView(withId(R.id.button_save_set)).perform(click())
    }

    @Test
    fun testClickSaveWithInvalidWeightAndReps_toastDisplayed() {
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        val scenario = launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText(""))
        onView(withId(R.id.button_save_set)).perform(click())

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