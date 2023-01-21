package com.example.lightweight.ui.cycleplanning

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        }

        buttonCancelNewTrainingCycle.setOnClickListener {
            cancel()
        }
    }
}