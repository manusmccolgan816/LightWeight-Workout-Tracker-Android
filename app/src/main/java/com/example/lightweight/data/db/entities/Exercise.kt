package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "EXERCISE",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        childColumns = ["category_ID"],
        parentColumns = ["category_ID"],
        onDelete = CASCADE // When a category is deleted, all its child exercises are too
    )]
)
data class Exercise(
    @ColumnInfo(name = "exercise_name")
    val exerciseName: String,

    @ColumnInfo(name = "category_ID")
    val categoryID: Int?
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_ID")
    var exerciseID: Int? = null
}