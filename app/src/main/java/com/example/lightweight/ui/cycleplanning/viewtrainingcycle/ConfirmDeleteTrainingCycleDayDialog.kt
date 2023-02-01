package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay

class ConfirmDeleteTrainingCycleDayDialog(
    context: Context,
    private val cycleDay: CycleDay,
    val deleteCycleDay: (CycleDay) -> Unit
) : AppCompatDialog(context) {

    private lateinit var buttonConfirmDeleteTrainingCycleDay: Button
    private lateinit var buttonCancelDeleteTrainingCycleDay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_delete_training_cycle_day)

        buttonConfirmDeleteTrainingCycleDay =
            findViewById(R.id.button_confirm_delete_training_cycle_day)!!
        buttonCancelDeleteTrainingCycleDay =
            findViewById(R.id.button_cancel_delete_training_cycle_day)!!

        buttonConfirmDeleteTrainingCycleDay.setOnClickListener {
            deleteCycleDay(cycleDay)
            dismiss()
        }

        buttonCancelDeleteTrainingCycleDay.setOnClickListener {
            cancel()
        }
    }
}