package com.example.lightweight.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lightweight.R

class SelectTrainingCycleFragment : Fragment(R.layout.fragment_select_training_cycle) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the action bar title
        activity?.title = resources.getString(R.string.string_plan_training_cycles)
    }
}