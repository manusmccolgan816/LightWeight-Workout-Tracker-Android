package com.example.lightweight.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "TRAINING_SET",
    foreignKeys = [ForeignKey(
        entity = ExerciseInstance::class,
        childColumns = ["exercise_instance_ID"],
        parentColumns = ["exercise_instance_ID"],
        onDelete = CASCADE
    )]
)
class TrainingSet(
    @ColumnInfo(name = "exercise_instance_ID")
    var exerciseInstanceID: Int?,

    @ColumnInfo(name = "training_set_number")
    var trainingSetNumber: Int,

    @ColumnInfo(name = "weight")
    var weight: Float,

    @ColumnInfo(name = "reps")
    var reps: Int,

    @ColumnInfo(name = "note")
    var note: String?,

    @ColumnInfo(name = "is_PR")
    var isPR: Boolean
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "training_set_ID")
    var trainingSetID: Int? = null
}