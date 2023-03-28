package com.example.lightweight.ui.workouttracking.calendar

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import com.example.lightweight.data.repositories.FakeWorkoutRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CalendarFragmentTest {
    @Test
    fun testCalendarFragmentInView() {
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)

        val args = bundleOf("selectedDate" to "2022-12-03")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel
        )
        launchFragmentInContainer<CalendarFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_calendar)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavController_navigateToHomeFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val fakeWorkoutRepository = FakeWorkoutRepository()
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)

        val args = bundleOf("selectedDate" to "2022-12-03")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel
        )
        val scenario = launchFragmentInContainer<CalendarFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.calendarFragment)
        }

        onView(withId(R.id.recycler_view_calendar))
            .perform(actionOnItemAtPosition<CalendarViewHolder>(6, click()))

        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.homeFragment)
    }

    @Test
    fun testClickDate_navigateToHomeFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.image_view_select_date)).perform(click())

        onView(withId(R.id.constraint_layout_calendar)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view_calendar))
            .perform(actionOnItemAtPosition<CalendarViewHolder>(15, click()))

        onView(withId(R.id.constraint_layout_home)).check(matches(isDisplayed()))
    }

    @Test
    fun testBackPress_navigateToHomeFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.image_view_select_date)).perform(click())

        onView(withId(R.id.constraint_layout_calendar)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.constraint_layout_home)).check(matches(isDisplayed()))
    }

    @Test
    fun testClickNextMonth_displayNextMonth() {
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)

        val args = bundleOf("selectedDate" to "2022-03-17")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel
        )
        launchFragmentInContainer<CalendarFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.text_view_month_year)).check(matches(withText("March 2022")))

        onView(withId(R.id.image_view_prev_month)).perform(click())

        onView(withId(R.id.text_view_month_year)).check(matches(withText("February 2022")))
    }

    @Test
    fun testClickPrevMonth_displayPreviousMonth() {
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)

        val args = bundleOf("selectedDate" to "2022-03-17")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel
        )
        launchFragmentInContainer<CalendarFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.text_view_month_year)).check(matches(withText("March 2022")))

        onView(withId(R.id.image_view_next_month)).perform(click())

        onView(withId(R.id.text_view_month_year)).check(matches(withText("April 2022")))
    }
}