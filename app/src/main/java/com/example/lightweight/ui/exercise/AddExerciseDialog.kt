package com.example.lightweight.ui.exercise

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.Exercise

class AddExerciseDialog(
    context: Context,
    val category: Category,
    val addExercise:(Exercise) -> Unit,
) : AppCompatDialog(context) {

    private lateinit var textViewNewExerciseCategoryName: TextView
    private lateinit var editTextNewExerciseName: EditText
    private lateinit var buttonSaveNewExercise: Button
    private lateinit var buttonCancelNewExercise: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_exercise)

        textViewNewExerciseCategoryName = findViewById(R.id.text_view_new_exercise_category_name)!!
        textViewNewExerciseCategoryName.text = category.categoryName

        editTextNewExerciseName = findViewById(R.id.edit_text_new_exercise_name)!!

        buttonSaveNewExercise = findViewById(R.id.button_save_new_exercise)!!
        buttonSaveNewExercise.setOnClickListener {
            val name = editTextNewExerciseName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No exercise name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exercise = Exercise(name, category.categoryID)
            addExercise(exercise)
            Toast.makeText(context, "$name exercise has been created under category: " +
                    category.categoryName, Toast.LENGTH_SHORT).show()
            dismiss()
        }

        buttonCancelNewExercise = findViewById(R.id.button_cancel_new_exercise)!!
        buttonCancelNewExercise.setOnClickListener {
            cancel()
        }
    }
}