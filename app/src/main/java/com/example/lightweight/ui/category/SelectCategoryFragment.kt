package com.example.lightweight.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectCategoryFragment : Fragment(R.layout.fragment_select_category), KodeinAware {

    override val kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()

    private val args: SelectCategoryFragmentArgs by navArgs()

    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var fabAddCategory: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: CategoryViewModel by viewModels { factory }
        val adapter = CategoryItemAdapter(args.selectedDate, listOf(), viewModel, this)

        // Set the action bar title
        activity?.title = "Select Category"

        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCategories.adapter = adapter

        viewModel.getAllCategories().observe(viewLifecycleOwner) {
            adapter.categories = it
            adapter.notifyDataSetChanged()
        }

        fabAddCategory = view.findViewById(R.id.fab_add_category)
        fabAddCategory.setOnClickListener {
            // Display the add category dialog
            AddCategoryDialog(
                requireContext(),
                fun(category: Category) { viewModel.insert(category) }
            ).show()
        }
    }
}