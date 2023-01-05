package com.example.lightweight.ui.settracker.logsets

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet

class EditTrainingSetDialog(
    context: Context,
    val trainingSet: TrainingSet,
    val editTrainingSet: (newWeight: Float, newReps: Int) -> Unit
) : AppCompatDialog(context) {

    private lateinit var editTextEditSetWeight: EditText
    private lateinit var editTextEditSetReps: EditText
    private lateinit var buttonSaveEditSet: Button
    private lateinit var buttonCancelEditSet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_training_set)

        editTextEditSetWeight = findViewById(R.id.edit_text_edit_set_weight)!!
        editTextEditSetReps = findViewById(R.id.edit_text_edit_set_reps)!!
        buttonSaveEditSet = findViewById(R.id.button_save_edit_set)!!
        buttonCancelEditSet = findViewById(R.id.button_cancel_edit_set)!!

        editTextEditSetWeight.setText(trainingSet.weight.toString())
        editTextEditSetReps.setText(trainingSet.reps.toString())

        buttonSaveEditSet.setOnClickListener {
            val weight = editTextEditSetWeight.text.toString()
            val reps = editTextEditSetReps.text.toString()

            if (weight.isBlank() || reps.isBlank()) {
                Toast.makeText(context, "Enter weight and reps", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Only call editTrainingSet if the set has been changed
            if (weight.toFloat() != trainingSet.weight || reps.toInt() != trainingSet.reps) {
                editTrainingSet(weight.toFloat(), reps.toInt())
            }

            dismiss()
        }

        buttonCancelEditSet.setOnClickListener {
            cancel()
        }
    }
}