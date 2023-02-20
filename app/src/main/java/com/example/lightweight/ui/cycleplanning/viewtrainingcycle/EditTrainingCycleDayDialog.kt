package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay

class EditTrainingCycleDayDialog(
    context: Context,
    private val cycleDay: CycleDay,
    private val updateCycleDay: (CycleDay) -> Unit
) : AppCompatDialog(context) {

    private lateinit var editTextEditTrainingCycleDayName: EditText
    private lateinit var buttonSaveEditTrainingCycleDay: Button
    private lateinit var buttonCancelEditTrainingCycleDay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_training_cycle_day)

        editTextEditTrainingCycleDayName =
            findViewById(R.id.edit_text_edit_training_cycle_day_name)!!
        buttonSaveEditTrainingCycleDay = findViewById(R.id.button_save_edit_training_cycle_day)!!
        buttonCancelEditTrainingCycleDay =
            findViewById(R.id.button_cancel_edit_training_cycle_day)!!

        editTextEditTrainingCycleDayName.setText(cycleDay.cycleDayName)

        buttonSaveEditTrainingCycleDay.setOnClickListener {
            val name = editTextEditTrainingCycleDayName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedCycleDay = CycleDay(cycleDay.cycleID, name, cycleDay.cycleDayNumber)
            updatedCycleDay.cycleDayID = cycleDay.cycleDayID
            updateCycleDay(updatedCycleDay)

            dismiss()
        }

        buttonCancelEditTrainingCycleDay.setOnClickListener {
            cancel()
        }
    }
}