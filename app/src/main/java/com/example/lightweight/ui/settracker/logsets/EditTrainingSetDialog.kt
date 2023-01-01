package com.example.lightweight.ui.settracker.logsets

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet

class EditTrainingSetDialog(
    context: Context,
    val trainingSet: TrainingSet,
    val editTrainingSet: (trainingSetID: Int?, newWeight: Float, newReps: Int) -> Unit
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

        }

        buttonCancelEditSet.setOnClickListener {
            cancel()
        }
    }
}