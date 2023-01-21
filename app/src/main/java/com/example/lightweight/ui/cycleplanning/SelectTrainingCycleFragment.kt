package com.example.lightweight.ui.cycleplanning

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle

class SelectTrainingCycleFragment : Fragment(R.layout.fragment_select_training_cycle) {

    private lateinit var recyclerViewTrainingCycles: RecyclerView
    private lateinit var fabAddTrainingCycle: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the action bar title
        activity?.title = resources.getString(R.string.string_plan_training_cycles)

        recyclerViewTrainingCycles = view.findViewById(R.id.recycler_view_training_cycles)
        fabAddTrainingCycle = view.findViewById(R.id.extended_fab_add_training_cycle)

        fabAddTrainingCycle.setOnClickListener {
            // Display the add training cycle dialog
            AddTrainingCycleDialog(requireContext(), fun(cycle: Cycle) {  }).show()
        }
    }
}