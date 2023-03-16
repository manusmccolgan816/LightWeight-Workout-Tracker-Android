package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "CYCLE_DAY_EXERCISE",
    foreignKeys = [
        ForeignKey(
            entity = CycleDayCategory::class,
            childColumns = ["cycle_day_category_ID"],
            parentColumns = ["cycle_day_category_ID"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            childColumns = ["exercise_ID"],
            parentColumns = ["exercise_ID"],
            onDelete = CASCADE
        )
    ]
)
data class CycleDayExercise(
    @ColumnInfo(name = "cycle_day_category_ID")
    var cycleDayCategoryID: Int?,

    @ColumnInfo(name = "exercise_ID")
    var exerciseID: Int?,

    @ColumnInfo(name = "cycle_day_exercise_number")
    var cycleDayExerciseNumber: Int
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cycle_day_exercise_ID")
    var cycleDayExerciseID: Int? = null
}