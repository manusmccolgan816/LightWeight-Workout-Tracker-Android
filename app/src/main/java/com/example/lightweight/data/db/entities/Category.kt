package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="CATEGORY")
data class Category (
    @ColumnInfo(name = "category_name")
    val categoryName: String
    ) {

    @PrimaryKey(autoGenerate = true)
    var categoryID: Int? = null
}