package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
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
            .check(matches(isDisplayed()))
    }

    @Test
    fun testClickSaveWithValidWeightAndReps_trainingSetAddedToRecyclerView() {
        // TODO: REQUIRES FAKE REPOSITORY TO BE USED
        // This test requires the unit of measurement to be kg

        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        //val fragment: LogSetsFragment = testKodein.direct.instance()
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("12"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("12")))

        onView(withId(R.id.button_save_set)).perform(click())

        onView(withId(R.id.recycler_view_training_sets))
            .perform(
                actionOnItemAtPosition<TrainingSetItemAdapter.TrainingSetItemViewHolder>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("12 reps")))
            )
        onView(withId(R.id.recycler_view_training_sets))
            .perform(
                actionOnItemAtPosition<TrainingSetItemAdapter.TrainingSetItemViewHolder>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("10.0kg")))
            )
    }

    @Test
    fun testClickSaveWithInvalidWeightAndReps_toastDisplayed() {
        // TODO: CAN'T GET TOASTMATCHER TO WORK
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText(""))
        onView(withId(R.id.edit_text_num_reps)).perform(replaceText(""))
        onView(withId(R.id.button_save_set)).perform(click())
//        onView(withText("Enter weight and reps")).inRoot(ToastMatcher())
//            .check(matches(isDisplayed()))
    }

    @Test
    fun testClickClear_weightAndRepsCleared() {
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("12"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("12")))

        onView(withId(R.id.button_clear_set)).perform(click())
        onView(withId(R.id.edit_text_weight)).check(matches(withText("")))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("")))
    }

    @Test
    fun testClickDecrementWeight_weightDecremented() {
        // This test requires the weight increment to be the default value of 2.5

        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))
        onView(withId(R.id.image_view_reduce_weight)).perform(click())
        onView(withId(R.id.edit_text_weight)).check(matches(withText("7.5")))
    }

    @Test
    fun testClickIncrementWeight_weightIncremented() {
        // This test requires the weight increment to be the default value of 2.5

        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))
        onView(withId(R.id.image_view_add_weight)).perform(click())
        onView(withId(R.id.edit_text_weight)).check(matches(withText("12.5")))
    }

    @Test
    fun testClickDecrementReps_repsDecremented() {
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("10")))
        onView(withId(R.id.image_view_reduce_reps)).perform(click())
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("9")))
    }

    @Test
    fun testClickIncrementReps_repsIncremented() {
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-03"
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("10")))
        onView(withId(R.id.image_view_add_reps)).perform(click())
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("11")))
    }

    @Test
    fun testSavePrTrainingSet_trophyDisplayedBesideRecyclerViewItem() {
        // TODO: REQUIRES FAKE REPOSITORY TO BE USED
        val args = bundleOf(
            "exerciseID" to 1,
            "selectedDate" to "2022-12-02"
        )
        //val fragment: LogSetsFragment = testKodein.direct.instance()
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
        )
    }
}