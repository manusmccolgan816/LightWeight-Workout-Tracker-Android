package com.example.lightweight.ui.workouttracking.selectcategory

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category

class AddCategoryDialog(context: Context, val addCategory: (Category) -> Unit) :
    AppCompatDialog(context) {

    private lateinit var editTextNewCategoryName: EditText
    private lateinit var buttonSaveNewCategory: Button
    private lateinit var buttonCancelNewCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_category)

        editTextNewCategoryName = findViewById(R.id.edit_text_new_category_name)!!
        buttonSaveNewCategory = findViewById(R.id.button_save_new_category)!!
        buttonCancelNewCategory = findViewById(R.id.button_cancel_new_category)!!

        buttonSaveNewCategory.setOnClickListener {
            val name = editTextNewCategoryName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No category name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = Category(name)
            addCategory(category)
            Toast.makeText(
                context, "$name category has been created",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        buttonCancelNewCategory.setOnClickListener {
            cancel()
        }
    }
}