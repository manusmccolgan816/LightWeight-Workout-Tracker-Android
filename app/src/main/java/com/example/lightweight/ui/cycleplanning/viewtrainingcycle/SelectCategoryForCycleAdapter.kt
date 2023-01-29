package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category

class SelectCategoryForCycleAdapter(
    var categories: List<Category>,
    val selectCategory: (Category) -> Unit
) : RecyclerView.Adapter<SelectCategoryForCycleAdapter.SelectCategoryForCycleViewHolder>() {

    private lateinit var parent: ViewGroup

    private lateinit var textViewCategoryName: TextView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectCategoryForCycleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_for_cycle, parent, false)
        this.parent = parent
        return SelectCategoryForCycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectCategoryForCycleViewHolder, position: Int) {
        val curCategory = categories[position]

        textViewCategoryName = holder.itemView.findViewById(R.id.text_view_category_name)

        textViewCategoryName.text = curCategory.categoryName

        textViewCategoryName.setOnClickListener {
            selectCategory(curCategory)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class SelectCategoryForCycleViewHolder(view: View) : RecyclerView.ViewHolder(view)
}