package com.example.lightweight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.ui.WorkoutViewModel

class CategoryItemAdapter(
    var categories: List<Category>,
    private val viewModel: WorkoutViewModel
) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val curCategory = categories[position]

        holder.itemView.findViewById<TextView>(R.id.text_view_category_name)
            .text = curCategory.categoryName
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryItemViewHolder(categoryView: View): RecyclerView.ViewHolder(categoryView)
}