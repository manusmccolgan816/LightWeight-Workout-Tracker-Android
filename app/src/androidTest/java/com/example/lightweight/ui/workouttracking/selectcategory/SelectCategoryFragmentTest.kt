package com.example.lightweight.ui.workouttracking.selectcategory

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
import org.junit.Test
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.repositories.FakeCategoryRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.category.CategoryViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SelectCategoryFragmentTest {
    @Test
    fun testSelectCategoryFragmentInView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val testCategoryViewModel = CategoryViewModel(fakeCategoryRepository)

        val args = bundleOf("selectedDate" to "2022-12-03")
        val factory = LightweightFragmentFactory(categoryViewModel = testCategoryViewModel)
        launchFragmentInContainer<SelectCategoryFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_select_category)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavController_navigateToSelectExerciseFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val fakeCategoryRepository = FakeCategoryRepository()
        val category = Category("Test Cat")

        runBlocking {
            fakeCategoryRepository.insert(category)
        }

        val testCategoryViewModel = CategoryViewModel(fakeCategoryRepository)

        val args = bundleOf("selectedDate" to "2022-12-03")
        val factory = LightweightFragmentFactory(categoryViewModel = testCategoryViewModel)
        val scenario = launchFragmentInContainer<SelectCategoryFragment>(
            themeResId = R.style.Theme_Lightweight, // Set the theme to avoid inflation error
            fragmentArgs = args,
            factory = factory
        )

        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
            navController.setCurrentDestination(R.id.selectCategoryFragment, args)
        }

        onView(withContentDescription("List of categories"))
            .perform(actionOnItemAtPosition<CategoryItemAdapter.CategoryItemViewHolder>(0, click()))

        Truth.assertThat(navController.currentDestination?.id)
            .isEqualTo(R.id.selectExerciseFragment)
    }

    @Test
    fun testClickCategory_navigateToSelectExerciseFragmentOfCategory() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_add_exercises)).perform(click())

        onView(withId(R.id.constraint_layout_select_category)).check(matches(isDisplayed()))

        onView(withContentDescription("List of categories"))
            .perform(actionOnItemAtPosition<CategoryItemAdapter.CategoryItemViewHolder>(1, click()))

        onView(withId(R.id.constraint_layout_select_exercise)).check(matches(isDisplayed()))
    }

    @Test
    fun testBackPress_navigateToHomeFragment() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.button_add_exercises)).perform(click())

        onView(withId(R.id.constraint_layout_select_category)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.constraint_layout_home)).check(matches(isDisplayed()))
    }
}