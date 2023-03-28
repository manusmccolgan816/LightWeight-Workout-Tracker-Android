package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.workouttracking.selectcategory.AddCategoryDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddTrainingCycleDayCategoryDialogFragment(
    val addCycleDayCategory: (Category) -> Unit,
    private val categoryViewModel: CategoryViewModel
) : DialogFragment() {

    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var fabAddCategory: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.dialog_add_training_cycle_day_category, container, false)

        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
        fabAddCategory = view.findViewById(R.id.fab_add_category)

        val adapter = SelectCategoryForCycleAdapter(
            listOf(),
            fun(category: Category) {
                addCycleDayCategory(category)
                dismiss()
            },
            this,
            categoryViewModel
        )
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCategories.adapter = adapter

        categoryViewModel.getAllCategories().observe(viewLifecycleOwner) {
            adapter.categories = it
            adapter.notifyDataSetChanged()
        }

        fabAddCategory.setOnClickListener {
            AddCategoryDialog(
                requireContext(),
                fun(category: Category) { categoryViewModel.insert(category) }
            ).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}