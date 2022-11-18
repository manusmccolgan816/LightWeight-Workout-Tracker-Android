package com.example.lightweight.ui

import com.example.lightweight.data.db.entities.Category

interface AddCategoryDialogListener {
    fun onAddButtonClicked(category: Category)
}