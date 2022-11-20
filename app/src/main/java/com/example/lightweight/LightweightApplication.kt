package com.example.lightweight

import android.app.Application
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.repositories.WorkoutRepository
import com.example.lightweight.ui.category.CategoryViewModelFactory
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
        bind() from singleton { WorkoutRepository(instance()) }
        // provider instantiates a new instance each time a reference is made
        bind() from provider { CategoryViewModelFactory(instance()) }
    }
}