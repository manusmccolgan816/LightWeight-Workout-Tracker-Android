package com.example.lightweight.ui.category

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.ui.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectCategoryFragment : Fragment(R.layout.fragment_select_category), KodeinAware {

    override val kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()

    private val args: SelectCategoryFragmentArgs by navArgs()

    private var categories = listOf<Category>()

    lateinit var searchViewCategories: SearchView
    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var fabAddCategory: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: CategoryViewModel by viewModels { factory }

        if (activity!!::class == MainActivity::class) {
            // Set the toolbar title
            val textViewToolbarTitle =
                requireActivity().findViewById<TextView>(R.id.text_view_toolbar_title)
            textViewToolbarTitle.text = resources.getString(R.string.string_select_category)

            // Remove the share icon
            val imageViewShareWorkout =
                activity?.findViewById(R.id.image_view_share_workout) as ImageView
            imageViewShareWorkout.visibility = View.GONE

            // Remove the select date icon
            val imageViewSelectDate =
                activity?.findViewById(R.id.image_view_select_date) as ImageView
            imageViewSelectDate.visibility = View.GONE
        }

        searchViewCategories = view.findViewById(R.id.search_view_categories)
        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
        fabAddCategory = view.findViewById(R.id.fab_add_category)

        val adapter = CategoryItemAdapter(
            args.selectedDate,
            listOf(),
            viewModel,
            this,
            fun(categoryId: Int, selectedDate: String) {
                val action = SelectCategoryFragmentDirections
                    .actionSelectCategoryFragmentToSelectExerciseFragment(
                        categoryId, selectedDate
                    )
                findNavController().navigate(action)
            }
        )
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCategories.adapter = adapter

        viewModel.getAllCategories().observe(viewLifecycleOwner) {
            categories = it
            adapter.categories = it
            adapter.notifyDataSetChanged()
        }

        searchViewCategories.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // If text has been input...
                if (newText != null) {
                    val filteredList: ArrayList<Category> = ArrayList()

                    for (category: Category in categories) {
                        if (category.categoryName.lowercase().contains(newText.lowercase())) {
                            filteredList.add(category)
                        }
                    }

                    adapter.categories = filteredList
                    adapter.notifyDataSetChanged()
                }

                return true
            }
        })

        fabAddCategory.setOnClickListener {
            // Display the add category dialog
            AddCategoryDialog(
                requireContext(),
                fun(category: Category) {
                    viewModel.insert(category)
                    // Clear the search view text
                    searchViewCategories.setQuery("", false)
                    // Ensure the keyboard does not appear
                    searchViewCategories.clearFocus()
                }
            ).show()
        }
    }
}