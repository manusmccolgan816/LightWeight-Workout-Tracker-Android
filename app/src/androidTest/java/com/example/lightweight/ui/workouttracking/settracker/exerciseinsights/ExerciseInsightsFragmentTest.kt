package com.example.lightweight.ui.workouttracking.settracker.exerciseinsights

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.*
import com.example.lightweight.data.repositories.*
import com.example.lightweight.ui.LightweightFragmentFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ExerciseInsightsFragmentTest {
    @Test
    fun testExerciseInsightsFragmentInView() {
        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(FakeExerciseInstanceRepository())
        val testTrainingSetViewModel = TrainingSetViewModel(FakeTrainingSetRepository())

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.constraint_layout_exercise_insights)).check(matches(isDisplayed()))
    }

    @Test
    fun testInstancesStatCorrect() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val workout1 = Workout("2022-12-03", null)
        workout1.workoutID = 1
        val workout2 = Workout("2022-12-06", null)
        workout2.workoutID = 2
        val exerciseInstance1 = ExerciseInstance(
            workout1.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance1.exerciseInstanceID = 1
        val exerciseInstance2 = ExerciseInstance(
            workout2.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance2.exerciseInstanceID = 2
        val trainingSet1 = TrainingSet(
            exerciseInstance1.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            1,
            100f,
            7,
            null,
            false
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout1)
            fakeWorkoutRepository.insert(workout2)
            fakeExerciseInstanceRepository.insert(exerciseInstance1)
            fakeExerciseInstanceRepository.insert(exerciseInstance2)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.text_view_total_instances_value)).check(matches(withText("2")))
    }

    @Test
    fun testTotalSetsStatCorrect() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val workout1 = Workout("2022-12-03", null)
        workout1.workoutID = 1
        val workout2 = Workout("2022-12-06", null)
        workout2.workoutID = 2
        val exerciseInstance1 = ExerciseInstance(
            workout1.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance1.exerciseInstanceID = 1
        val exerciseInstance2 = ExerciseInstance(
            workout2.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance2.exerciseInstanceID = 2
        val trainingSet1 = TrainingSet(
            exerciseInstance1.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            1,
            100f,
            7,
            null,
            false
        )
        val trainingSet3 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            2,
            105f,
            5,
            null,
            true
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout1)
            fakeWorkoutRepository.insert(workout2)
            fakeExerciseInstanceRepository.insert(exerciseInstance1)
            fakeExerciseInstanceRepository.insert(exerciseInstance2)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
            fakeTrainingSetRepository.insert(trainingSet3)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.text_view_total_sets_value)).check(matches(withText("3")))
    }

    @Test
    fun testTotalRepsStatCorrect() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val workout1 = Workout("2022-12-03", null)
        workout1.workoutID = 1
        val workout2 = Workout("2022-12-06", null)
        workout2.workoutID = 2
        val exerciseInstance1 = ExerciseInstance(
            workout1.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance1.exerciseInstanceID = 1
        val exerciseInstance2 = ExerciseInstance(
            workout2.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance2.exerciseInstanceID = 2
        val trainingSet1 = TrainingSet(
            exerciseInstance1.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            1,
            100f,
            7,
            null,
            false
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout1)
            fakeWorkoutRepository.insert(workout2)
            fakeExerciseInstanceRepository.insert(exerciseInstance1)
            fakeExerciseInstanceRepository.insert(exerciseInstance2)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.text_view_total_reps_value)).check(matches(withText("15")))
    }

    @Test
    fun testMaxWeightStatCorrect() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val workout1 = Workout("2022-12-03", null)
        workout1.workoutID = 1
        val workout2 = Workout("2022-12-06", null)
        workout2.workoutID = 2
        val exerciseInstance1 = ExerciseInstance(
            workout1.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance1.exerciseInstanceID = 1
        val exerciseInstance2 = ExerciseInstance(
            workout2.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance2.exerciseInstanceID = 2
        val trainingSet1 = TrainingSet(
            exerciseInstance1.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            1,
            100f,
            7,
            null,
            false
        )
        val trainingSet3 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            2,
            105f,
            5,
            null,
            true
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout1)
            fakeWorkoutRepository.insert(workout2)
            fakeExerciseInstanceRepository.insert(exerciseInstance1)
            fakeExerciseInstanceRepository.insert(exerciseInstance2)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
            fakeTrainingSetRepository.insert(trainingSet3)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.text_view_max_weight_value)).check(matches(withText("105.0")))
    }

    @Test
    fun testMaxRepsStatCorrect() {
        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val workout1 = Workout("2022-12-03", null)
        workout1.workoutID = 1
        val workout2 = Workout("2022-12-06", null)
        workout2.workoutID = 2
        val exerciseInstance1 = ExerciseInstance(
            workout1.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance1.exerciseInstanceID = 1
        val exerciseInstance2 = ExerciseInstance(
            workout2.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance2.exerciseInstanceID = 2
        val trainingSet1 = TrainingSet(
            exerciseInstance1.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            1,
            100f,
            7,
            null,
            false
        )
        val trainingSet3 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            2,
            105f,
            5,
            null,
            true
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout1)
            fakeWorkoutRepository.insert(workout2)
            fakeExerciseInstanceRepository.insert(exerciseInstance1)
            fakeExerciseInstanceRepository.insert(exerciseInstance2)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
            fakeTrainingSetRepository.insert(trainingSet3)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.text_view_max_reps_value)).check(matches(withText("8")))
    }

    @Test
    fun testPRsStatCorrect() {
        // This test requires the unit of measurement to be the default value of kg

        val fakeCategoryRepository = FakeCategoryRepository()
        val fakeExerciseRepository = FakeExerciseRepository()
        val fakeWorkoutRepository = FakeWorkoutRepository()
        val fakeExerciseInstanceRepository = FakeExerciseInstanceRepository()
        val fakeTrainingSetRepository = FakeTrainingSetRepository()

        val category = Category("Test Cat")
        category.categoryID = 1
        val exercise = Exercise("Test Ex", category.categoryID)
        exercise.exerciseID = 1
        val workout1 = Workout("2022-12-03", null)
        workout1.workoutID = 1
        val workout2 = Workout("2022-12-06", null)
        workout2.workoutID = 2
        val exerciseInstance1 = ExerciseInstance(
            workout1.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance1.exerciseInstanceID = 1
        val exerciseInstance2 = ExerciseInstance(
            workout2.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance2.exerciseInstanceID = 2
        val trainingSet1 = TrainingSet(
            exerciseInstance1.exerciseInstanceID,
            1,
            100f,
            8,
            null,
            true
        )
        val trainingSet2 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            1,
            100f,
            7,
            null,
            false
        )
        val trainingSet3 = TrainingSet(
            exerciseInstance2.exerciseInstanceID,
            2,
            105f,
            5,
            null,
            true
        )

        runBlocking {
            fakeExerciseRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.exerciseInstanceRepo = fakeExerciseInstanceRepository
            fakeWorkoutRepository.trainingSetRepo = fakeTrainingSetRepository
            fakeExerciseInstanceRepository.trainingSetRepo = fakeTrainingSetRepository

            fakeCategoryRepository.insert(category)
            fakeExerciseRepository.insert(exercise)
            fakeWorkoutRepository.insert(workout1)
            fakeWorkoutRepository.insert(workout2)
            fakeExerciseInstanceRepository.insert(exerciseInstance1)
            fakeExerciseInstanceRepository.insert(exerciseInstance2)
            fakeTrainingSetRepository.insert(trainingSet1)
            fakeTrainingSetRepository.insert(trainingSet2)
            fakeTrainingSetRepository.insert(trainingSet3)
        }

        val testExerciseInstanceViewModel =
            ExerciseInstanceViewModel(fakeExerciseInstanceRepository)
        val testTrainingSetViewModel = TrainingSetViewModel(fakeTrainingSetRepository)

        val args = bundleOf("exerciseID" to 1)
        val factory = LightweightFragmentFactory(
            exerciseInstanceViewModel = testExerciseInstanceViewModel,
            trainingSetViewModel = testTrainingSetViewModel,
        )
        launchFragmentInContainer<ExerciseInsightsFragment>(
            fragmentArgs = args,
            themeResId = R.style.Theme_Lightweight,
            factory = factory
        )

        onView(withId(R.id.card_view_personal_records)).perform(click())

        // Check the size of the recycler view
        onView(withId(R.id.recycler_view_personal_records)).check(
            matches(
                AndroidTestUtil.recyclerViewSizeMatcher(
                    2
                )
            )
        )

        // Check that the PRs displayed have the correct weight and reps
        onView(withId(R.id.recycler_view_personal_records))
            .perform(actionOnItemAtPosition<PersonalRecordAdapter.PersonalRecordViewHolder>(
                0,
                scrollTo()
            )).check(matches(hasDescendant(withText("5RM"))))
        onView(withId(R.id.recycler_view_personal_records))
            .perform(actionOnItemAtPosition<PersonalRecordAdapter.PersonalRecordViewHolder>(
                0,
                scrollTo()
            )).check(matches(hasDescendant(withText("105.0kg"))))
        onView(withId(R.id.recycler_view_personal_records))
            .perform(actionOnItemAtPosition<PersonalRecordAdapter.PersonalRecordViewHolder>(
                1,
                scrollTo()
            )).check(matches(hasDescendant(withText("8RM"))))
        onView(withId(R.id.recycler_view_personal_records))
            .perform(actionOnItemAtPosition<PersonalRecordAdapter.PersonalRecordViewHolder>(
                1,
                scrollTo()
            )).check(matches(hasDescendant(withText("100.0kg"))))
    }
}





