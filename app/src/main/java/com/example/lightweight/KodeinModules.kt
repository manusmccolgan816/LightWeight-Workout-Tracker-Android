package com.example.lightweight

import com.example.lightweight.data.repositories.*
import com.example.lightweight.ui.category.CategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModelFactory
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.example.lightweight.ui.workouttracking.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.workouttracking.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workouttracking.workout.WorkoutViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val testCategoryViewModelFactory = CategoryViewModelFactory(FakeCategoryRepository())
val testExerciseViewModelFactory = ExerciseViewModelFactory(FakeExerciseRepository())
val testWorkoutViewModelFactory = WorkoutViewModelFactory(FakeWorkoutRepository())
val testExerciseInstanceViewModelFactory =
    ExerciseInstanceViewModelFactory(FakeExerciseInstanceRepository())
val testTrainingSetViewModelFactory = TrainingSetViewModelFactory(FakeTrainingSetRepository())
val testCycleViewModelFactory = CycleViewModelFactory(FakeCycleRepository())

val testModule = Kodein.Module("testModule") {
    bind<CategoryRepositoryInterface>() with singleton { FakeCategoryRepository() }
    bind() from provider { CategoryViewModelFactory(instance()) }

    bind<ExerciseRepositoryInterface>() with singleton { FakeExerciseRepository() }
    bind() from provider { ExerciseViewModelFactory(instance()) }

    bind<WorkoutRepositoryInterface>() with singleton { FakeWorkoutRepository() }
    bind() from provider { WorkoutViewModelFactory(instance()) }

    bind<ExerciseInstanceRepositoryInterface>() with singleton {
        FakeExerciseInstanceRepository()
    }
    bind() from provider { ExerciseInstanceViewModelFactory(instance()) }

    bind<TrainingSetRepositoryInterface>() with singleton { FakeTrainingSetRepository() }
    bind() from provider { TrainingSetViewModelFactory(instance()) }


//    bind() from singleton { FakeTrainingSetRepository() }
//    // provider instantiates a new instance each time a reference is made
//    bind() from provider { TrainingSetViewModelFactory(instance()) }
//    bind<CategoryViewModelFactory>() with singleton { testCategoryViewModelFactory }
//    bind<ExerciseViewModelFactory>() with singleton { testExerciseViewModelFactory }
//    bind<WorkoutViewModelFactory>() with singleton { testWorkoutViewModelFactory }
//    bind<ExerciseInstanceViewModelFactory>() with singleton { testExerciseInstanceViewModelFactory }
//    bind<TrainingSetViewModelFactory>() with singleton { testTrainingSetViewModelFactory }
}