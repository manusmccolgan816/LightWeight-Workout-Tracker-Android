package com.example.lightweight.ui.cycleplanning

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle

class EditTrainingCycleDialog(
    context: Context,
    val cycle: Cycle,
    val editCycle: (editedCycle: Cycle) -> Unit,
) : AppCompatDialog(context) {

    private lateinit var editTextEditTrainingCycleName: EditText
    private lateinit var editTextEditTrainingCycleDesc: EditText
    private lateinit var buttonSaveEditTrainingCycle: Button
    private lateinit var buttonCancelEditTrainingCycle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_training_cycle)

        editTextEditTrainingCycleName = findViewById(R.id.edit_text_edit_training_cycle_name)!!
        editTextEditTrainingCycleDesc = findViewById(R.id.edit_text_edit_training_cycle_desc)!!
        buttonSaveEditTrainingCycle = findViewById(R.id.button_save_edit_training_cycle)!!
        buttonCancelEditTrainingCycle = findViewById(R.id.button_cancel_edit_training_cycle)!!

        editTextEditTrainingCycleName.setText(cycle.cycleName)
        editTextEditTrainingCycleDesc.setText(cycle.description)

        buttonSaveEditTrainingCycle.setOnClickListener {
            val name = editTextEditTrainingCycleName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var desc: String? = editTextEditTrainingCycleDesc.text.toString().trim()
            // Ensure that an empty desc is stored as a null value
            if (desc!!.isEmpty()) desc = null

            Toast.makeText(
                context,
                "${cycle.cycleName} has been renamed to $name",
                Toast.LENGTH_SHORT
            ).show()

            val editedCycle = Cycle(name, desc)
            editedCycle.cycleID = cycle.cycleID
            // Call the passed lambda function to edit the given cycle
            editCycle(editedCycle)
            dismiss()
        }

        buttonCancelEditTrainingCycle.setOnClickListener {
            cancel()
        }
    }
}