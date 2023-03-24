package com.example.lightweight.ui.cycleplanning.selecttrainingcycle

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectTrainingCycleFragment : Fragment(R.layout.fragment_select_training_cycle), KodeinAware {

    override val kodein by kodein()
    private val cycleFactory: CycleViewModelFactory by instance()
    private val cycleViewModel: CycleViewModel by viewModels { cycleFactory }

    private lateinit var recyclerViewTrainingCycles: RecyclerView
    private lateinit var fabAddTrainingCycle: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.activity is MainActivity) {
            // Set the toolbar title
            val textViewToolbarTitle = requireActivity().findViewById<TextView>(R.id.text_view_toolbar_title)
            textViewToolbarTitle.text = resources.getString(R.string.string_plan_training_cycles)

            // Remove the share icon
            val imageViewShareWorkout = activity?.findViewById(R.id.image_view_share_workout) as ImageView
            imageViewShareWorkout.visibility = View.GONE

            // Remove the select date icon
            val imageViewSelectDate = activity?.findViewById(R.id.image_view_select_date) as ImageView
            imageViewSelectDate.visibility = View.GONE
        }

        recyclerViewTrainingCycles = view.findViewById(R.id.recycler_view_training_cycles)
        fabAddTrainingCycle = view.findViewById(R.id.extended_fab_add_training_cycle)

        val adapter = TrainingCycleItemAdapter(listOf(), this)
        recyclerViewTrainingCycles.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingCycles.adapter = adapter

        cycleViewModel.getAllCycles().observe(viewLifecycleOwner) {
            adapter.cycles = it
            adapter.notifyDataSetChanged()
        }

        fabAddTrainingCycle.setOnClickListener {
            // Display the add training cycle dialog
            AddTrainingCycleDialog(
                requireContext(),
                fun(cycle: Cycle) { cycleViewModel.insert(cycle) }).show()
        }
    }
}