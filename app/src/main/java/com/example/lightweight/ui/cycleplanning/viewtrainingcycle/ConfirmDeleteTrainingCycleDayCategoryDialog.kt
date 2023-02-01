package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R

class ConfirmDeleteTrainingCycleDayCategoryDialog(
    context: Context,
    private val cycleDayCategoryID: Int?,
    val deleteCycleDayCategory: (Int?) -> Unit
) : AppCompatDialog(context) {

    private lateinit var buttonConfirmDeleteTrainingCycleDayCategory: Button
    private lateinit var buttonCancelDeleteTrainingCycleDayCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_delete_training_cycle_day_category)

        buttonConfirmDeleteTrainingCycleDayCategory =
            findViewById(R.id.button_confirm_delete_training_cycle_day_category)!!
        buttonCancelDeleteTrainingCycleDayCategory =
            findViewById(R.id.button_cancel_delete_training_cycle_day_category)!!

        buttonConfirmDeleteTrainingCycleDayCategory.setOnClickListener {
            deleteCycleDayCategory(cycleDayCategoryID)
            dismiss()
        }

        buttonCancelDeleteTrainingCycleDayCategory.setOnClickListener {
            cancel()
        }
    }
}