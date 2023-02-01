package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R

class ConfirmDeleteTrainingCycleDayExerciseDialog(
    context: Context,
    private val cycleDayExerciseID: Int?,
    val deleteCycleDayExercise: (Int?) -> Unit
) : AppCompatDialog(context) {

    private lateinit var buttonConfirmDeleteTrainingCycleDayExercise: Button
    private lateinit var buttonCancelDeleteTrainingCycleDayExercise: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_delete_training_cycle_day_exercise)

        buttonConfirmDeleteTrainingCycleDayExercise =
            findViewById(R.id.button_confirm_delete_training_cycle_day_exercise)!!
        buttonCancelDeleteTrainingCycleDayExercise =
            findViewById(R.id.button_cancel_delete_training_cycle_day_exercise)!!

        buttonConfirmDeleteTrainingCycleDayExercise.setOnClickListener {
            deleteCycleDayExercise(cycleDayExerciseID)
            dismiss()
        }

        buttonCancelDeleteTrainingCycleDayExercise.setOnClickListener {
            cancel()
        }
    }
}