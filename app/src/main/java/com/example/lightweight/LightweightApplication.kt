package com.example.lightweight

import android.app.Application
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.repositories.*
import com.example.lightweight.ui.category.CategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModelFactory
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class LightweightApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@LightweightApplication))
        // singleton ensures that the instance never changes
        bind() from singleton { WorkoutDatabase(instance()) }

        bind() from singleton { CategoryRepository(instance()) }
        // provider instantiates a new instance each time a reference is made
        bind() from provider { CategoryViewModelFactory(instance()) }

        bind() from singleton { ExerciseRepository(instance()) }
        bind() from provider { ExerciseViewModelFactory(instance()) }

        bind() from singleton { WorkoutRepository(instance()) }
        bind() from provider { WorkoutViewModelFactory(instance()) }

        bind() from singleton { ExerciseInstanceRepository(instance()) }
        bind() from provider { ExerciseInstanceViewModelFactory(instance()) }

        bind() from singleton { TrainingSetRepository(instance()) }
        bind() from provider { TrainingSetViewModelFactory(instance()) }

        bind() from singleton { CycleRepository(instance()) }
        bind() from provider { CycleViewModelFactory(instance()) }

        bind() from singleton { CycleDayRepository(instance()) }
        bind() from provider { CycleDayViewModelFactory(instance()) }

        bind() from singleton { CycleDayCategoryRepository(instance()) }
        bind() from provider { CycleDayCategoryViewModelFactory(instance()) }

        bind() from singleton { CycleDayExerciseRepository(instance()) }
        bind() from provider { CycleDayExerciseViewModelFactory(instance()) }
    }
}