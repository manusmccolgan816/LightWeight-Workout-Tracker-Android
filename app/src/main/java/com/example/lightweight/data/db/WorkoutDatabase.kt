package com.example.lightweight.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lightweight.data.db.daos.CategoryDao
import com.example.lightweight.data.db.daos.ExerciseDao
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise

@Database(
    entities = [Category::class, Exercise::class],
    version = 3,
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getExerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var instance: WorkoutDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, WorkoutDatabase::class.java, "WorkoutDB.db")
            .fallbackToDestructiveMigration().build()
    }
}