package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "CYCLE_DAY_CATEGORY",
    foreignKeys = [ForeignKey(
        entity = CycleDay::class,
        childColumns = ["cycle_day_ID"],
        parentColumns = ["cycle_day_ID"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = Category::class,
        childColumns = ["category_ID"],
        parentColumns = ["category_ID"],
        onDelete = CASCADE
    )]
)
data class CycleDayCategory(
    @ColumnInfo(name = "cycle_day_ID")
    var cycleDayID: Int?,

    @ColumnInfo(name = "category_ID")
    var categoryID: Int?,

    @ColumnInfo(name = "cycle_day_category_number")
    var cycleDayCategoryNumber: Int
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cycle_day_category_ID")
    var cycleDayCategoryID: Int? = null
}