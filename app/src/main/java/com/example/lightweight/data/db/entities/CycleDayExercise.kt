package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "CYCLE_DAY_EXERCISE",
    foreignKeys = [ForeignKey(
        entity = CycleDay::class,
        childColumns = ["cycle_day_ID"],
        parentColumns = ["cycle_day_ID"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = Exercise::class,
        childColumns = ["exercise_ID"],
        parentColumns = ["exercise_ID"],
        onDelete = CASCADE
    )]
)
data class CycleDayExercise(
    @ColumnInfo(name = "cycle_day_ID")
    val cycleDayID: Int?,

    @ColumnInfo(name = "cycle_day_category_ID")
    val cycleDayCategoryID: Int?,

    @ColumnInfo(name = "exercise_ID")
    val exerciseID: Int?,

    @ColumnInfo(name = "cycle_day_exercise_number")
    val cycleDayExerciseNumber: Int
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cycle_day_exercise_ID")
    var cycleDayExerciseID: Int? = null
}