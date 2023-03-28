package com.example.lightweight.ui.workouttracking.home

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import com.example.lightweight.data.repositories.FakeExerciseInstanceRepository
import com.example.lightweight.data.repositories.FakeWorkoutRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {
    @Test
    fun testHomeFragmentInView() {
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()

        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)

        val args = bundleOf("selectedDate" to "today")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel
        )
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_home)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavController_navigateToSelectCategoryFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()

        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)

        val args = bundleOf("selectedDate" to "today")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel
        )
        val homeScenario = launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

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

    @Test
    fun testClickCalendar_navigateToCalendarFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.image_view_select_date)).perform(click())

        onView(withId(R.id.constraint_layout_calendar)).check(matches(isDisplayed()))
    }

    @Test
    fun testClickAddExercises_navigateToSelectCategoryFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_add_exercises)).perform(click())

        onView(withId(R.id.constraint_layout_select_category)).check(matches(isDisplayed()))
    }
}