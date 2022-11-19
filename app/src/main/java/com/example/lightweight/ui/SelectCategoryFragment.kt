package com.example.lightweight.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.lightweight.CategoryItemAdapter
import com.example.lightweight.R
import com.example.lightweight.data.db.WorkoutDatabase
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.repositories.WorkoutRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SelectCategoryFragment : Fragment(R.layout.fragment_select_category) {

    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var fabAddCategory: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creates the database with the current context - IllegalStateException is thrown if
        // context is null
        val database = WorkoutDatabase(requireContext())
        val repository = WorkoutRepository(database)
        val factory = WorkoutViewModelFactory(repository)

        val viewModel: WorkoutViewModel by viewModels { factory }

        val adapter = CategoryItemAdapter(listOf(), viewModel)

        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCategories.adapter = adapter

        viewModel.getAllCategories().observe(viewLifecycleOwner) {
            adapter.categories = it
            adapter.notifyDataSetChanged()
        }

        fabAddCategory = view.findViewById(R.id.fab_add_category)
        fabAddCategory.setOnClickListener {
            AddCategoryDialog(requireContext(), object : AddCategoryDialogListener {
                override fun onAddButtonClicked(category: Category) {
                    viewModel.upsert(category)
                }
            }).show()
        }
    }
}