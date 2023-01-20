package com.example.lightweight.ui.category

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category

class CategoryItemAdapter(
    private val selectedDate: String,
    var categories: List<Category>,
    private val viewModel: CategoryViewModel,
    var fragment: SelectCategoryFragment
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
        // Set up popup menu for each category item when icon is clicked
        imageViewCategoryOptions.setOnClickListener {
            // Create the popup menu anchored to the category item
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.category_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_category -> {
                        // Display the edit category dialog
                        EditCategoryDialog(parent.context, curCategory,
                            fun(categoryID: Int?, newName: String) {
                                viewModel.update(categoryID, newName)
                            }).show()
                        true
                    }
                    R.id.menu_item_delete_category -> {
                        // Display the delete category dialog
                        ConfirmDeleteCategoryDialog(parent.context, curCategory,
                            fun(category: Category) { viewModel.delete(category) }).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }

        // Navigate to SelectExerciseFragment when a category item is selected, passing the category
        // as a parameter
        holder.itemView.setOnClickListener {
            // Remove the search view text
            fragment.searchViewCategories.setQuery("", false)

            val action = SelectCategoryFragmentDirections
                .actionSelectCategoryFragmentToSelectExerciseFragment(
                    curCategory.categoryID!!, selectedDate
                )
            findNavController(fragment).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryItemViewHolder(categoryView: View) : RecyclerView.ViewHolder(categoryView)
}