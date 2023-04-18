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
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil.clickChildViewWithId
import com.example.lightweight.AndroidTestUtil.recyclerViewSizeMatcher
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.db.entities.Workout
import com.example.lightweight.data.repositories.FakeExerciseInstanceRepository
import com.example.lightweight.data.repositories.FakeExerciseRepository
import com.example.lightweight.data.repositories.FakeTrainingSetRepository
import com.example.lightweight.data.repositories.FakeWorkoutRepository
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {
    @Test
    fun testHomeFragmentInView() {
        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val args = bundleOf("selectedDate" to "today")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
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

        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val args = bundleOf("selectedDate" to "today")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        val scenario = launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        scenario.onFragment { fragment ->
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
    fun testNavController_navigateToLogSetsFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val exercise = Exercise("Test Ex", 1)
        exercise.exerciseID = 1
        val workout = Workout("2022-12-03", null)
        workout.workoutID = 1
        val exerciseInstance = ExerciseInstance(
            workout.workoutID, exercise.exerciseID, 1, null
        )
        exerciseInstance.exerciseInstanceID = 1
        val trainingSet1 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            2,
            100f,
            8,
            null,
            false
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout)
            fakeExerciseInstanceRepository.insert(exerciseInstance)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
        }

        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel = ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("selectedDate" to "3 December 2022")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        val scenario = launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        scenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Thread.sleep(1000)

        onView(withText(exercise.exerciseName)).perform(click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.setTrackerActivity)
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

    @Test
    fun testDeleteExerciseInstance_exerciseInstanceRemovedFromRecyclerView() {
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val exercise = Exercise("Test Ex", 1)
        exercise.exerciseID = 1
        val workout = Workout("2022-12-03", null)
        workout.workoutID = 1
        val exerciseInstance = ExerciseInstance(
            workout.workoutID, exercise.exerciseID, 1, null
        )
        exerciseInstance.exerciseInstanceID = 1
        val trainingSet1 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            2,
            100f,
            8,
            null,
            false
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout)
            fakeExerciseInstanceRepository.insert(exerciseInstance)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
        }

        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel = ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("selectedDate" to "3 December 2022")
        val factory = LightweightFragmentFactory(
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        Thread.sleep(1000)

        onView(withId(R.id.recycler_view_exercise_instances))
            .perform(
                actionOnItemAtPosition<HomeParentWorkoutAdapter.HomeParentWorkoutViewHolder>(
                    0,
                    clickChildViewWithId(R.id.image_view_exercise_instance_options)
                )
            )
        onView(withText("Delete")).perform(click())

        // Assert that the recycler view is empty
        onView(withId(R.id.recycler_view_exercise_instances)).check(
            matches(
                recyclerViewSizeMatcher(
                    0
                )
            )
        )
    }
}