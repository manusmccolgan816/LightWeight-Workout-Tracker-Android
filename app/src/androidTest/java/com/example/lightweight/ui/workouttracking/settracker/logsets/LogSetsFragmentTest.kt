package com.example.lightweight.ui.workouttracking.settracker.logsets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
import com.example.lightweight.getOrAwaitValue
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LogSetsFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testLogSetsFragmentInView() {
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            fakeExerciseRepository.insert(exercise)
        }

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_log_sets))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testClickSaveWithValidWeightAndReps_trainingSetSaved() {
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()
        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            // Add the exercise to exercise and exerciseInstance repos
            fakeExerciseRepository.insert(exercise)
            fakeExerciseInstanceRepository.exercises.add(exercise)

            fakeTrainingSetRepository.exerciseInstances = fakeExerciseInstanceRepository
                .observableExerciseInstances.value as MutableList<ExerciseInstance>
            fakeTrainingSetRepository.workouts =
                fakeWorkoutRepository.observableWorkouts.value as MutableList<Workout>
        }

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("12"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("12")))

        onView(withId(R.id.button_save_set)).perform(click())

        val trainingSet =
            testTrainingSetViewModel.getTrainingSetsOfExercise(exercise.exerciseID)
                .getOrAwaitValue()[0]
        Truth.assertThat(trainingSet.weight).isEqualTo(10f)
        Truth.assertThat(trainingSet.reps).isEqualTo(12)
    }

    @Test
    fun testClickClear_weightAndRepsCleared() {
        val fakeExerciseRepository = FakeExerciseRepository()

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            fakeExerciseRepository.insert(exercise)
        }

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
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

        val fakeExerciseRepository = FakeExerciseRepository()

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            fakeExerciseRepository.insert(exercise)
        }

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))
        onView(withId(R.id.image_view_reduce_weight)).perform(click())
        onView(withId(R.id.edit_text_weight)).check(matches(withText("7.5")))
    }

    @Test
    fun testClickIncrementWeight_weightIncremented() {
        // This test requires the weight increment to be the default value of 2.5

        val fakeExerciseRepository = FakeExerciseRepository()

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            fakeExerciseRepository.insert(exercise)
        }

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.edit_text_weight)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_weight)).check(matches(withText("10")))
        onView(withId(R.id.image_view_add_weight)).perform(click())
        onView(withId(R.id.edit_text_weight)).check(matches(withText("12.5")))
    }

    @Test
    fun testClickDecrementReps_repsDecremented() {
        val fakeExerciseRepository = FakeExerciseRepository()

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            fakeExerciseRepository.insert(exercise)
        }

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("10")))
        onView(withId(R.id.image_view_reduce_reps)).perform(click())
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("9")))
    }

    @Test
    fun testClickIncrementReps_repsIncremented() {
        val fakeExerciseRepository = FakeExerciseRepository()

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(FakeWorkoutRepository())
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            fakeExerciseRepository.insert(exercise)
        }

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.edit_text_num_reps)).perform(replaceText("10"))
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("10")))
        onView(withId(R.id.image_view_add_reps)).perform(click())
        onView(withId(R.id.edit_text_num_reps)).check(matches(withText("11")))
    }

    @Test
    fun testSavePrTrainingSet_trainingSetWithTrophyAddedToRecyclerView() {
        // This test requires the unit of measurement to be the default value of kg

        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()
        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1

        runBlocking {
            // Add the exercise to exercise and exerciseInstance repos
            fakeExerciseRepository.insert(exercise)
            fakeExerciseInstanceRepository.exercises.add(exercise)
        }

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
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
        onView(withId(R.id.recycler_view_training_sets))
            .perform(
                actionOnItemAtPosition<TrainingSetItemAdapter.TrainingSetItemViewHolder>(
                    0, scrollTo()
                )
            ).check(
                matches(hasDescendant(withId(R.id.image_view_trophy)))
            ).check(
                matches(isDisplayed())
            )
    }

    @Test
    fun testEditTrainingSet_trainingSetEditedInRecyclerView() {

    }

    @Test
    fun testDeleteTrainingSet_trainingSetRemovedFromRecyclerView() {
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val exercise = Exercise("Fake Exercise", 1)
        exercise.exerciseID = 1
        val workout = Workout("2022-12-03", null)
        workout.workoutID = 1
        val exerciseInstance = ExerciseInstance(
            workout.workoutID, exercise.exerciseID, 1, null
        )
        exerciseInstance.exerciseInstanceID = 1
        val trainingSet = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            1,
            100f,
            10,
            null,
            true
        )

        runBlocking {
            // Give some repos references to each other so that they can have up-to-date data
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout)
            fakeExerciseInstanceRepository.insert(exerciseInstance)
            fakeTrainingSetRepository.insert(trainingSet)
        }

        val testExerciseViewModel = ExerciseViewModel(fakeExerciseRepository)
        val testWorkoutViewModel = WorkoutViewModel(fakeWorkoutRepository)
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf(
            "exerciseID" to exercise.exerciseID,
            "selectedDate" to "2022-12-03"
        )
        val factory = LightweightFragmentFactory(
            exerciseViewModel = testExerciseViewModel,
            workoutViewModel = testWorkoutViewModel,
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel
        )
        launchFragmentInContainer<LogSetsFragment>(
            themeResId = R.style.Theme_Lightweight,
            fragmentArgs = args,
            factory = factory
        )

        onView(withId(R.id.recycler_view_training_sets))
            .perform(
                actionOnItemAtPosition<TrainingSetItemAdapter.TrainingSetItemViewHolder>(
                    0,
                    clickChildViewWithId(R.id.image_view_training_set_options)
                )
            )

        onView(withText("Delete")).perform(click())

        // Assert that the recycler view is empty
        onView(withId(R.id.recycler_view_training_sets)).check(
            matches(
                recyclerViewSizeMatcher(
                    0
                )
            )
        )
    }
}