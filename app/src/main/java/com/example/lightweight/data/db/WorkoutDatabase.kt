package com.example.lightweight.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lightweight.data.db.entities.Category

@Database(
    entities = [Category::class],
    version = 2,
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao

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