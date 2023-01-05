package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "TRAINING_SET",
    foreignKeys = [ForeignKey(
        entity = ExerciseInstance::class,
        childColumns = ["exercise_instance_ID"],
        parentColumns = ["exercise_instance_ID"],
        onDelete = CASCADE // When an exercise instance is deleted, so is the set
    )]
)
class TrainingSet(
    @ColumnInfo(name = "exercise_instance_ID")
    val exerciseInstanceID: Int?,

    @ColumnInfo(name = "training_set_number")
    val trainingSetNumber: Int,

    @ColumnInfo(name = "weight")
    val weight: Float,

    @ColumnInfo(name = "reps")
    val reps: Int,

    @ColumnInfo(name = "note")
    val note: String?,

    @ColumnInfo(name = "is_PR")
    val isPR: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "training_set_ID")
    var trainingSetID: Int? = null
}