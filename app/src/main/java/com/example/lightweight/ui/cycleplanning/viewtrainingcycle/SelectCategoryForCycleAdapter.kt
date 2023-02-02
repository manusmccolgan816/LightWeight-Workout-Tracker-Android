package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.category.CategoryViewModelFactory
import com.example.lightweight.ui.category.ConfirmDeleteCategoryDialog
import com.example.lightweight.ui.category.EditCategoryDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectCategoryForCycleAdapter(
    var categories: List<Category>,
    val selectCategory: (Category) -> Unit,
    val fragment: Fragment
) : RecyclerView.Adapter<SelectCategoryForCycleAdapter.SelectCategoryForCycleViewHolder>(),
    KodeinAware {

    override val kodein by kodein(fragment.requireContext())
    private val categoryFactory: CategoryViewModelFactory by instance()
    private val categoryViewModel: CategoryViewModel by fragment.viewModels { categoryFactory }

    private lateinit var parent: ViewGroup

    private lateinit var textViewCategoryName: TextView
    private lateinit var imageViewCategoryOptions: ImageView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectCategoryForCycleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        this.parent = parent
        return SelectCategoryForCycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectCategoryForCycleViewHolder, position: Int) {
        val curCategory = categories[position]

        textViewCategoryName = holder.itemView.findViewById(R.id.text_view_category_name)
        imageViewCategoryOptions = holder.itemView.findViewById(R.id.image_view_category_options)

        textViewCategoryName.text = curCategory.categoryName
        textViewCategoryName.setOnClickListener {
            selectCategory(curCategory)
        }

        imageViewCategoryOptions.setOnClickListener {
            val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.END)

            popupMenu.inflate(R.menu.category_popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_category -> {
                        EditCategoryDialog(
                            parent.context,
                            curCategory,
                            fun(categoryID: Int?, newName: String) {
                                categoryViewModel.update(categoryID, newName)
                            }
                        ).show()
                        true
                    }
                    R.id.menu_item_delete_category -> {
                        ConfirmDeleteCategoryDialog(
                            parent.context,
                            curCategory,
                            fun(category: Category) {
                                categoryViewModel.delete(category)
                            }
                        ).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class SelectCategoryForCycleViewHolder(view: View) : RecyclerView.ViewHolder(view)
}