package com.example.lightweight.ui.cycleplanning

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle

class AddTrainingCycleDialog(context: Context, val addTrainingCycle: (Cycle) -> Unit) :
    AppCompatDialog(context) {

    private lateinit var editTextNewTrainingCycleName: EditText
    private lateinit var editTextNewTrainingCycleDesc: EditText
    private lateinit var buttonSaveNewTrainingCycle: Button
    private lateinit var buttonCancelNewTrainingCycle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_training_cycle)

        editTextNewTrainingCycleName = findViewById(R.id.edit_text_new_training_cycle_name)!!
        editTextNewTrainingCycleDesc = findViewById(R.id.edit_text_new_training_cycle_desc)!!
        buttonSaveNewTrainingCycle = findViewById(R.id.button_save_new_training_cycle)!!
        buttonCancelNewTrainingCycle = findViewById(R.id.button_cancel_new_training_cycle)!!

        buttonSaveNewTrainingCycle.setOnClickListener {
            val name: String = editTextNewTrainingCycleName.text.toString().trim()
            var desc: String? = editTextNewTrainingCycleDesc.text.toString().trim()
            // If the user has not entered a description, set it to null
            if (desc.isNullOrEmpty()) {
                desc = null
            }

            if (name.isEmpty()) {
                Toast.makeText(context, "No name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cycle = Cycle(name, desc)
            addTrainingCycle(cycle)
            Toast.makeText(context, "$name training cycle has been created", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }

        buttonCancelNewTrainingCycle.setOnClickListener {
            cancel()
        }
    }
}