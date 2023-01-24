package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay

class AddTrainingCycleDayDialog(
    context: Context,
    val cycleID: Int?,
    val addTrainingCycleDay: (CycleDay) -> Unit
) : AppCompatDialog(context) {

    private lateinit var editTextNewTrainingCycleDayName: EditText
    private lateinit var buttonSaveNewTrainingCycleDay: Button
    private lateinit var buttonCancelNewTrainingCycleDay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_training_cycle_day)

        editTextNewTrainingCycleDayName = findViewById(R.id.edit_text_new_training_cycle_day_name)!!
        buttonSaveNewTrainingCycleDay = findViewById(R.id.button_save_new_training_cycle_day)!!
        buttonCancelNewTrainingCycleDay = findViewById(R.id.button_cancel_new_training_cycle_day)!!

        buttonSaveNewTrainingCycleDay.setOnClickListener {
            val name: String = editTextNewTrainingCycleDayName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO Ensure cycleDayNumber is calculated
            val cycleDay = CycleDay(cycleID, name, 0)
            addTrainingCycleDay(cycleDay)
            dismiss()
        }

        buttonCancelNewTrainingCycleDay.setOnClickListener {
            cancel()
        }
    }
}