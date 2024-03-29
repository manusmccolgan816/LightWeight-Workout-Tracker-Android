package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CATEGORY")
data class Category(
    @ColumnInfo(name = "category_name")
    var categoryName: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_ID")
    var categoryID: Int? = null
}