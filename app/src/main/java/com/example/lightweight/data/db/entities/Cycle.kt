package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CYCLE")
data class Cycle(
    @ColumnInfo(name = "cycle_name")
    val cycleName: String,

    @ColumnInfo(name = "description")
    val description: String?
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cycle_ID")
    var cycleID: Int? = null
}