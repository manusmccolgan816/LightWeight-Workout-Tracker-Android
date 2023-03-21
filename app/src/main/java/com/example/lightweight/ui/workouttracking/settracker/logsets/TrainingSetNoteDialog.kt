package com.example.lightweight.ui.workouttracking.settracker.logsets

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.TrainingSet

class TrainingSetNoteDialog(
    context: Context,
    val trainingSet: TrainingSet,
    val editNote: (trainingSetID: Int?, note: String?) -> Unit
) : AppCompatDialog(context) {

    private lateinit var editTextSetNote: EditText
    private lateinit var buttonSaveSetNote: Button
    private lateinit var buttonCancelSetNote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_training_set_note)

        editTextSetNote = findViewById(R.id.edit_text_set_note)!!
        buttonSaveSetNote = findViewById(R.id.button_save_set_note)!!
        buttonCancelSetNote = findViewById(R.id.button_cancel_set_note)!!

        // Set the text to the set's existing note if it is not null
        if (trainingSet.note != null) editTextSetNote.setText(trainingSet.note)

        buttonSaveSetNote.setOnClickListener {
            val note = editTextSetNote.text.toString().trim()

            editNote(trainingSet.trainingSetID, note)
            dismiss()
        }

        buttonCancelSetNote.setOnClickListener {
            cancel()
        }
    }
}