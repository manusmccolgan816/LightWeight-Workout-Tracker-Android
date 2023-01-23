package com.example.lightweight.ui.cycleplanning

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle

class ConfirmDeleteTrainingCycleDialog(
    context: Context,
    val cycle: Cycle,
    val deleteCycle: (Cycle) -> Unit
) : AppCompatDialog(context) {

    private lateinit var buttonConfirmDeleteTrainingCycle: Button
    private lateinit var buttonCancelDeleteTrainingCycle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_delete_training_cycle)

        buttonConfirmDeleteTrainingCycle = findViewById(R.id.button_confirm_delete_training_cycle)!!
        buttonCancelDeleteTrainingCycle = findViewById(R.id.button_cancel_delete_training_cycle)!!

        buttonConfirmDeleteTrainingCycle.setOnClickListener {
            deleteCycle(cycle)
            Toast.makeText(
                context, "${cycle.cycleName} has been deleted",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        buttonCancelDeleteTrainingCycle.setOnClickListener {
            cancel()
        }
    }
}