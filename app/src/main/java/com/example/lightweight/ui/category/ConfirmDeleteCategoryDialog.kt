package com.example.lightweight.ui.category

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category

class ConfirmDeleteCategoryDialog(
    context: Context,
    val category: Category,
    val deleteCategory: (Category) -> Unit
) : AppCompatDialog(context) {

    private lateinit var buttonConfirmDeleteCategory: Button
    private lateinit var buttonCancelDeleteCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_delete_category)

        buttonConfirmDeleteCategory = findViewById(R.id.button_confirm_delete_category)!!
        buttonConfirmDeleteCategory.setOnClickListener {
            deleteCategory(category)
            Toast.makeText(
                context, "${category.categoryName} has been deleted",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        buttonCancelDeleteCategory = findViewById(R.id.button_cancel_delete_category)!!
        buttonCancelDeleteCategory.setOnClickListener {
            cancel()
        }
    }
}