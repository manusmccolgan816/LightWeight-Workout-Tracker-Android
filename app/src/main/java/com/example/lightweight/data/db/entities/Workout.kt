package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WORKOUT")
class Workout (
    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "note")
    val note: String?
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_ID")
    var categoryID: Int? = null
}