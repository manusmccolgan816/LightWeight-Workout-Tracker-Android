package com.example.lightweight.ui.workouttracking.selectexercise

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
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.repositories.FakeCategoryRepository
import com.example.lightweight.data.repositories.FakeExerciseRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.workouttracking.selectcategory.CategoryItemAdapter
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SelectExerciseFragmentTest {
    @Test
    fun testSelectExerciseFragmentInView() {
        val testCategoryViewModel = CategoryViewModel(FakeCategoryRepository())
        val testExerciseViewModel = ExerciseViewModel(FakeExerciseRepository())

        val args = bundleOf(
            "categoryID" to 1,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            categoryViewModel = testCategoryViewModel,
            exerciseViewModel = testExerciseViewModel
        )
        launchFragmentInContainer<SelectExerciseFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_select_exercise))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testNavController_navigateToSetTrackerActivity() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val category = Category("Test Cat")
        category.categoryID = 1

        runBlocking {
            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(Exercise("Test Ex", category.categoryID))
        }

        val testCategoryViewModel = CategoryViewModel(fakeCategoryRepository)
        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)

        val args = bundleOf(
            "categoryID" to category.categoryID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            categoryViewModel = testCategoryViewModel,
            exerciseViewModel = testExerciseViewModel
        )
        val scenario = launchFragmentInContainer<SelectExerciseFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.selectExerciseFragment, args)
        }

        onView(withContentDescription("List of exercises"))
            .perform(actionOnItemAtPosition<ExerciseItemAdapter.ExerciseItemViewHolder>(0, click()))

        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.setTrackerActivity)
    }

    @Test
    fun testClickExercise_navigateToLogSetsFragmentOfExercise() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_add_exercises)).perform(click())

        onView(withContentDescription("List of categories"))
            .perform(actionOnItemAtPosition<CategoryItemAdapter.CategoryItemViewHolder>(1, click()))

        onView(withId(R.id.constraint_layout_select_exercise)).check(matches(isDisplayed()))

        onView(withContentDescription("List of exercises"))
            .perform(actionOnItemAtPosition<CategoryItemAdapter.CategoryItemViewHolder>(1, click()))

        onView(withId(R.id.relative_layout_set_tracker)).check(matches(isDisplayed()))
    }

    @Test
    fun testBackPress_navigateToSelectCategoryFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_add_exercises)).perform(click())

        onView(withContentDescription("List of categories"))
            .perform(actionOnItemAtPosition<CategoryItemAdapter.CategoryItemViewHolder>(1, click()))

        onView(withId(R.id.constraint_layout_select_exercise)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.constraint_layout_select_category)).check(matches(isDisplayed()))
    }
}