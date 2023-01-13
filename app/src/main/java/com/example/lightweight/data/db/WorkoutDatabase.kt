package com.example.lightweight.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lightweight.data.db.daos.*
import com.example.lightweight.data.db.entities.*

@Database(
    entities = [
        Category::class,
        Exercise::class,
        Workout::class,
        ExerciseInstance::class,
        TrainingSet::class
    ],
    version = 7,
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getExerciseDao(): ExerciseDao
    abstract fun getWorkoutDao(): WorkoutDao
    abstract fun getExerciseInstanceDao(): ExerciseInstanceDao
    abstract fun getTrainingSetDao(): TrainingSetDao

    companion object {
        @Volatile
        private var instance: WorkoutDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, WorkoutDatabase::class.java, "WorkoutDB.db")
            .createFromAsset("database/lightweightdb.db").build()
    }
}