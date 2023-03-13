package com.example.lightweight.ui.calendar

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.openLinkWithText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Assert.*
import com.example.lightweight.R
import com.example.lightweight.ui.home.HomeFragment
import com.google.common.truth.Truth

import org.junit.Test

class CalendarFragmentTest {
    @Test
    fun testIsCalendarFragmentInView() {
        val args = bundleOf("selectedDate" to "2022-12-03")
        val scenario = launchFragmentInContainer<CalendarFragment>(args)

        onView(withId(R.id.constraint_layout_calendar)).check(matches(isDisplayed()))
    }

    @Test
    fun testSelectDate_navigateToHomeFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val args = bundleOf("selectedDate" to "2022-12-03")
        // Create a graphical FragmentScenario for HomeFragment
        val scenario = launchFragmentInContainer<CalendarFragment>(args)

        scenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Click RecyclerView item and verify that it triggers navigation to HomeFragment
        onView(withId(R.id.recycler_view_calendar))
            .perform(actionOnItemAtPosition<CalendarViewHolder>(2, click()))
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.homeFragment)
    }
}