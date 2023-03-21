package com.example.lightweight.ui.workouttracking.selectexercise

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise

class ConfirmDeleteExerciseDialog(
    context: Context,
    val exercise: Exercise,
    val deleteExercise: (Exercise) -> Unit
) : AppCompatDialog(context) {

    private lateinit var buttonConfirmDeleteExercise: Button
    private lateinit var buttonCancelDeleteExercise: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_delete_exercise)

        buttonConfirmDeleteExercise = findViewById(R.id.button_confirm_delete_exercise)!!
        buttonConfirmDeleteExercise.setOnClickListener {
            deleteExercise(exercise)
            Toast.makeText(
                context, "${exercise.exerciseName} has been deleted",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        buttonCancelDeleteExercise = findViewById(R.id.button_cancel_delete_exercise)!!
        buttonCancelDeleteExercise.setOnClickListener {
            cancel()
        }
    }
}