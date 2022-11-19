package com.example.lightweight

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.ui.WorkoutViewModel

class CategoryItemAdapter(
    var categories: List<Category>,
    private val viewModel: WorkoutViewModel
) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {

    private lateinit var parent: ViewGroup
    private lateinit var imageViewCategoryOptions: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        this.parent = parent
        return CategoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val curCategory = categories[position]

        holder.itemView.findViewById<TextView>(R.id.text_view_category_name)
            .text = curCategory.categoryName

        imageViewCategoryOptions = holder.itemView.findViewById(R.id.image_view_category_options)

        // Setting up popup menu for each category item when icon is clicked
        imageViewCategoryOptions.setOnClickListener{
            // Create the popup menu anchored to the category item
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.category_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_category -> {
                        Toast.makeText(parent.context, "Clicked and all", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_item_delete_category -> {
                        true
                    }
                    else -> { true }
                }
            }

            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryItemViewHolder(categoryView: View): RecyclerView.ViewHolder(categoryView)
}