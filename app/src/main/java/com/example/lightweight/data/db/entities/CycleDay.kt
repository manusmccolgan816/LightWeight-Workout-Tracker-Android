package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "CYCLE_DAY",
    foreignKeys = [ForeignKey(
        entity = Cycle::class,
        childColumns = ["cycle_ID"],
        parentColumns = ["cycle_ID"],
        onDelete = CASCADE
    )]
)
data class CycleDay(
    @ColumnInfo(name = "cycle_ID")
    val cycleID: Int?,

    @ColumnInfo(name = "cycle_day_name")
    val cycleDayName: String,

    @ColumnInfo(name = "cycle_day_number")
    val cycleDayNumber: Int
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cycle_day_ID")
    var cycleDayID: Int? = null
}