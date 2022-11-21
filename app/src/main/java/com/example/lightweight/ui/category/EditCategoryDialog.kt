package com.example.lightweight.ui.category

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category

class EditCategoryDialog(
    context: Context,
    val category: Category,
    val editCategory:(categoryID: Int?, newName: String) -> Unit
) : AppCompatDialog(context) {

    private lateinit var editTextEditCategoryName: EditText
    private lateinit var buttonSaveEditCategory: Button
    private lateinit var buttonCancelEditCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_category)

        editTextEditCategoryName = findViewById(R.id.edit_text_edit_category_name)!!
        // Set the text to the current category name
        editTextEditCategoryName.setText(category.categoryName)

        buttonSaveEditCategory = findViewById(R.id.button_save_edit_category)!!
        buttonSaveEditCategory.setOnClickListener {
            val name = editTextEditCategoryName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(context, "No category name given", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(context, "${category.categoryName} has been renamed to $name",
                Toast.LENGTH_SHORT).show()
            // Call the passed lambda function, giving the categoryID and the new name
            editCategory(category.categoryID, name)
            dismiss()
        }

        buttonCancelEditCategory = findViewById(R.id.button_cancel_edit_category)!!
        buttonCancelEditCategory.setOnClickListener {
            cancel()
        }
    }
}