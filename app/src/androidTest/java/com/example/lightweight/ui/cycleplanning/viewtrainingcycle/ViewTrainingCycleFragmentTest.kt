package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil.clickChildViewWithId
import com.example.lightweight.AndroidTestUtil.longClickChildViewWithId
import com.example.lightweight.AndroidTestUtil.recyclerViewSizeMatcher
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
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1
        val cycleDay = CycleDay(cycle.cycleID, "Test Day", 1)
        cycleDay.cycleDayID = 1
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeCategoryRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeExerciseRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeCycleRepository.insert(cycle)
            fakeCycleDayRepository.insert(cycleDay)
            fakeCycleDayCategoryRepository.insert(cycleDayCategory)
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
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayCategoryViewHolder>(
                    1,
                    clickChildViewWithId(R.id.image_view_add_exercise)
                )
            )

        onView(withId(R.id.recycler_view_exercises))
            .perform(
                actionOnItemAtPosition<SelectExerciseForCycleAdapter.SelectExerciseForCycleViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.recycler_view_training_cycle_days))
            .perform(
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayExerciseViewHolder>(
                    2,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText(exercise.exerciseName)))
            )
    }

    @Test
    fun testEditCycleDay_cycleDayEditedInRecyclerView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1
        val cycleDay = CycleDay(cycle.cycleID, "Test Day", 1)
        cycleDay.cycleDayID = 1
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeCategoryRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeExerciseRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeCycleRepository.insert(cycle)
            fakeCycleDayRepository.insert(cycleDay)
            fakeCycleDayCategoryRepository.insert(cycleDayCategory)
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
                    longClickChildViewWithId(R.id.text_view_training_cycle_day_name)
                )
            )

        onView(withText("Edit")).perform(click())
        onView(withId(R.id.edit_text_edit_training_cycle_day_name)).perform(replaceText("Edited Day"))
        onView(withId(R.id.button_save_edit_training_cycle_day)).perform(click())

        onView(withId(R.id.recycler_view_training_cycle_days))
            .perform(
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayViewHolder>(
                    0,
                    scrollTo()
                )
            ).check(
                matches(hasDescendant(withText("Edited Day")))
            )
    }

    @Test
    fun testRemoveCycleDay_cycleDayAndChildrenRemovedFromRecyclerView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1
        val cycleDay = CycleDay(cycle.cycleID, "Test Day", 1)
        cycleDay.cycleDayID = 1
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        val cycleDayExercise = CycleDayExercise(
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise.cycleDayExerciseID = 1

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeCategoryRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeExerciseRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeCycleRepository.insert(cycle)
            fakeCycleDayRepository.insert(cycleDay)
            fakeCycleDayCategoryRepository.insert(cycleDayCategory)
            fakeCycleDayExerciseRepository.insert(cycleDayExercise)
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
                    clickChildViewWithId(R.id.image_view_delete_day)
                )
            )

        onView(withId(R.id.button_confirm_delete_training_cycle_day)).perform(click())

        onView(withId(R.id.recycler_view_training_cycle_days)).check(
            matches(
                recyclerViewSizeMatcher(
                    0
                )
            )
        )
    }

    @Test
    fun testRemoveCycleDayCategory_cycleDayCategoryAndChildrenRemovedFromRecyclerView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1
        val cycleDay = CycleDay(cycle.cycleID, "Test Day", 1)
        cycleDay.cycleDayID = 1
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        val cycleDayExercise = CycleDayExercise(
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise.cycleDayExerciseID = 1

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeCategoryRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeExerciseRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeCycleRepository.insert(cycle)
            fakeCycleDayRepository.insert(cycleDay)
            fakeCycleDayCategoryRepository.insert(cycleDayCategory)
            fakeCycleDayExerciseRepository.insert(cycleDayExercise)
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
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayCategoryViewHolder>(
                    1,
                    clickChildViewWithId(R.id.image_view_delete_category)
                )
            )

        onView(withId(R.id.button_confirm_delete_training_cycle_day_category)).perform(click())

        // Check there is 1 item in the recycler view
        onView(withId(R.id.recycler_view_training_cycle_days)).check(
            matches(
                recyclerViewSizeMatcher(
                    1
                )
            )
        )
    }

    @Test
    fun testRemoveCycleDayExercise_cycleDayExerciseRemovedFromRecyclerView() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeCycleRepository = FakeCycleRepository()
        val fakeCycleDayRepository = FakeCycleDayRepository()
        val fakeCycleDayCategoryRepository = FakeCycleDayCategoryRepository()
        val fakeCycleDayExerciseRepository = FakeCycleDayExerciseRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val cycle = Cycle("Test Cycle", null)
        cycle.cycleID = 1
        val cycleDay = CycleDay(cycle.cycleID, "Test Day", 1)
        cycleDay.cycleDayID = 1
        val cycleDayCategory = CycleDayCategory(cycleDay.cycleDayID, category.categoryID, 1)
        cycleDayCategory.cycleDayCategoryID = 1
        val cycleDayExercise = CycleDayExercise(
            cycleDayCategory.cycleDayCategoryID,
            exercise.exerciseID,
            1
        )
        cycleDayExercise.cycleDayExerciseID = 1

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeCategoryRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeExerciseRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayRepository.cycleDayCategoryRepo = fakeCycleDayCategoryRepository
            fakeCycleDayRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository
            fakeCycleDayCategoryRepository.cycleDayExerciseRepo = fakeCycleDayExerciseRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeCycleRepository.insert(cycle)
            fakeCycleDayRepository.insert(cycleDay)
            fakeCycleDayCategoryRepository.insert(cycleDayCategory)
            fakeCycleDayExerciseRepository.insert(cycleDayExercise)
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
                actionOnItemAtPosition<TrainingCycleDayAdapter.TrainingCycleDayExerciseViewHolder>(
                    2,
                    clickChildViewWithId(R.id.image_view_delete_exercise)
                )
            )

        onView(withId(R.id.button_confirm_delete_training_cycle_day_exercise)).perform(click())

        // Check there are 2 items in the recycler view
        onView(withId(R.id.recycler_view_training_cycle_days)).check(
            matches(
                recyclerViewSizeMatcher(
                    2
                )
            )
        )
    }
}