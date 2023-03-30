package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil
import com.example.lightweight.AndroidTestUtil.clickChildViewWithId
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.*
import com.example.lightweight.data.repositories.*
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ViewTrainingCycleFragmentTest {
    @Test
    fun testViewTrainingCycleFragmentInView() {
        val fakeCycleRepository = FakeCycleRepository()
        val cycle = Cycle("Test Cycle", null)

        runBlocking {
            fakeCycleRepository.insert(cycle)
        }

        val testCategoryViewModel = CategoryViewModel(FakeCategoryRepository())
        val testExerciseViewModel = ExerciseViewModel(FakeExerciseRepository())
        val testCycleViewModel = CycleViewModel(fakeCycleRepository)
        val testCycleDayViewModel = CycleDayViewModel(FakeCycleDayRepository())
        val testCycleDayCategoryViewModel =
            CycleDayCategoryViewModel(FakeCycleDayCategoryRepository())
        val testCycleDayExerciseViewModel =
            CycleDayExerciseViewModel(FakeCycleDayExerciseRepository())

        val args = bundleOf("cycleID" to 0)
        val factory = LightweightFragmentFactory(
            categoryViewModel = testCategoryViewModel,
            exerciseViewModel = testExerciseViewModel,
            cycleViewModel = testCycleViewModel,
            cycleDayViewModel = testCycleDayViewModel,
            cycleDayCategoryViewModel = testCycleDayCategoryViewModel,
            cycleDayExerciseViewModel = testCycleDayExerciseViewModel
        )
        launchFragmentInContainer<ViewTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_view_training_cycle)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddCycleDay_cycleDayAddedToRecyclerView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1

        runBlocking {
            fakeCycleRepository.insert(cycle)

            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
        }

        val testCategoryViewModel = CategoryViewModel(fakeCategoryRepository)
        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testCycleViewModel = CycleViewModel(fakeCycleRepository)
        val testCycleDayViewModel = CycleDayViewModel(fakeCycleDayRepository)
        val testCycleDayCategoryViewModel =
            CycleDayCategoryViewModel(fakeCycleDayCategoryRepository)
        val testCycleDayExerciseViewModel =
            CycleDayExerciseViewModel(fakeCycleDayExerciseRepository)

        val args = bundleOf("cycleID" to cycle.cycleID)
        val factory = LightweightFragmentFactory(
            categoryViewModel = testCategoryViewModel,
            exerciseViewModel = testExerciseViewModel,
            cycleViewModel = testCycleViewModel,
            cycleDayViewModel = testCycleDayViewModel,
            cycleDayCategoryViewModel = testCycleDayCategoryViewModel,
            cycleDayExerciseViewModel = testCycleDayExerciseViewModel
        )
        launchFragmentInContainer<ViewTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.fab_add_training_cycle_day)).perform(click())
        onView(withId(R.id.edit_text_new_training_cycle_day_name)).perform(replaceText("Test Day"))
        onView(withId(R.id.button_save_new_training_cycle_day)).perform(click())

        onView(withId(R.id.recycler_view_training_cycle_days))
            .perform(
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayViewHolder>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("Test Day")))
            )
    }

    @Test
    fun testAddCycleDayCategory_cycleDayCategoryAddedToRecyclerView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1
        val cycleDay = CycleDay(cycle.cycleID, "Test Day", 1)
        cycleDay.cycleDayID = 1

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeCategoryRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository

            fakeCategoryRepository.insert(category)
            fakeCycleRepository.insert(cycle)
            fakeCycleDayRepository.insert(cycleDay)
        }

        val testCategoryViewModel = CategoryViewModel(fakeCategoryRepository)
        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testCycleViewModel = CycleViewModel(fakeCycleRepository)
        val testCycleDayViewModel = CycleDayViewModel(fakeCycleDayRepository)
        val testCycleDayCategoryViewModel =
            CycleDayCategoryViewModel(fakeCycleDayCategoryRepository)
        val testCycleDayExerciseViewModel =
            CycleDayExerciseViewModel(fakeCycleDayExerciseRepository)

        val args = bundleOf("cycleID" to cycle.cycleID)
        val factory = LightweightFragmentFactory(
            categoryViewModel = testCategoryViewModel,
            exerciseViewModel = testExerciseViewModel,
            cycleViewModel = testCycleViewModel,
            cycleDayViewModel = testCycleDayViewModel,
            cycleDayCategoryViewModel = testCycleDayCategoryViewModel,
            cycleDayExerciseViewModel = testCycleDayExerciseViewModel
        )
        launchFragmentInContainer<ViewTrainingCycleFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.recycler_view_training_cycle_days))
            .perform(
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayViewHolder>(
                    0,
                    clickChildViewWithId(R.id.image_view_add_category)
                )
            )

        onView(withId(R.id.recycler_view_categories))
            .perform(
                actionOnItemAtPosition<SelectCategoryForCycleAdapter.SelectCategoryForCycleViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.recycler_view_training_cycle_days))
            .perform(
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayCategoryViewHolder>(
                    1,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText(category.categoryName)))
            )
    }

    @Test
    fun testAddCycleDayExercise_cycleDayExerciseAddedToRecyclerView() {

    }

    @Test
    fun testEditCycleDay_cycleDayEditedInRecyclerView() {

    }

    @Test
    fun testRemoveCycleDay_cycleDayAndChildrenRemovedFromRecyclerView() {

    }

    @Test
    fun testRemoveCycleDayCategory_cycleDayCategoryAndChildrenRemovedFromRecyclerView() {

    }

    @Test
    fun testRemoveCycleDayExercise_cycleDayExerciseRemovedFromRecyclerView() {

    }
}