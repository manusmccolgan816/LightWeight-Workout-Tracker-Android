package com.example.lightweight.ui.exercise

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise

class EditExerciseDialog(
    context: Context,
    val exercise: Exercise,
    val editExercise:(exerciseID: Int?, newName: String) -> Unit
) : AppCompatDialog(context) {

    private lateinit var editTextEditExerciseName: EditText
    private lateinit var buttonSaveEditExercise: Button
    private lateinit var buttonCancelEditExercise: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_exercise)

        editTextEditExerciseName = findViewById(R.id.edit_text_edit_exercise_name)!!
        // Set the text to the current category name
        editTextEditExerciseName.setText(exercise.exerciseName)

        buttonSaveEditExercise = findViewById(R.id.button_save_edit_exercise)!!
        buttonSaveEditExercise.setOnClickListener {
            val name = editTextEditExerciseName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No exercise name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(context, "${exercise.exerciseName} has been renamed to $name",
                Toast.LENGTH_SHORT).show()
            // Call the passed lambda function, giving the categoryID and the new name
            editExercise(exercise.exerciseID, name)
            dismiss()
        }

        buttonCancelEditExercise = findViewById(R.id.button_cancel_edit_exercise)!!
        buttonCancelEditExercise.setOnClickListener {
            cancel()
        }
    }
}