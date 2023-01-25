package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.category.CategoryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AddTrainingCycleDayCategoryDialogFragment(

) : DialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val categoryFactory: CategoryViewModelFactory by instance()
    private val categoryViewModel: CategoryViewModel by viewModels { categoryFactory }

    private lateinit var recyclerViewCategories: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_training_cycle_day_category, container, false)

        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)

        val adapter = SelectCategoryForCycleAdapter(listOf())
        recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCategories.adapter = adapter

        categoryViewModel.getAllCategories().observe(viewLifecycleOwner) {
            adapter.categories = it
            adapter.notifyDataSetChanged()
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