package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "EXERCISE_INSTANCE",
    foreignKeys = [ForeignKey(
        entity = Workout::class,
        childColumns = ["workout_ID"],
        parentColumns = ["workout_ID"],
        onDelete = CASCADE // When a workout is deleted, all its instances are too
    ), ForeignKey(
        entity = Exercise::class,
        childColumns = ["exercise_ID"],
        parentColumns = ["exercise_ID"],
        onDelete = CASCADE // When an exercise is deleted, so are its instances
    )]
)
data class ExerciseInstance(
    @ColumnInfo(name = "workout_ID")
    val workoutID: Int?,

    @ColumnInfo(name = "exercise_ID")
    val exerciseID: Int?,

    @ColumnInfo(name = "exercise_instance_number")
    val exerciseInstanceNumber: Int,

    @ColumnInfo(name = "note")
    val note: String?
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_instance_ID")
    var exerciseInstanceID: Int? = null
}