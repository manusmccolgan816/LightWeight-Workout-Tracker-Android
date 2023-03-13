package com.example.lightweight.ui.home

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.example.lightweight.R
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.IdNamePair
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {
    @Test
    fun testIsHomeFragmentInView() {
        val args = bundleOf("selectedDate" to "today")
        val scenario = launchFragmentInContainer<HomeFragment>(args)

        onView(withId(R.id.constraint_layout_home)).check(matches(isDisplayed()))
    }

    @Test
    fun testDismissShareWorkoutDialogFragment() {
        val args = bundleOf(
            "selectedDate" to LocalDate.now(),
            "idNamePairs" to listOf<IdNamePair>(),
            "fragment" to null
        )
        with(launchFragment<ShareWorkoutDialogFragment>(args)) {
            onFragment { fragment ->
                assertThat(fragment.dialog).isNotNull()
                assertThat(fragment.requireDialog().isShowing).isTrue()
                fragment.dismiss()
                fragment.parentFragmentManager.executePendingTransactions()
                assertThat(fragment.dialog).isNull()
            }
        }

        onView(withText("Cancel")).check(doesNotExist())
    }

    @Test
    fun testNavigationToSelectCategoryFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val args = bundleOf("selectedDate" to "today")
        // Create a graphical FragmentScenario for HomeFragment
        val homeScenario = launchFragmentInContainer<HomeFragment>(args)

        homeScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController's state
        onView(withId(R.id.button_add_exercises)).perform(click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.selectCategoryFragment)
    }
}