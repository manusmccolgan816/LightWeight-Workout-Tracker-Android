package com.example.lightweight.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lightweight.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var fabCalendar: FloatingActionButton
    private lateinit var extendedFabAddExercises: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabCalendar = view.findViewById(R.id.fab_calendar)
        fabCalendar.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCalendarFragment()
            findNavController().navigate(action)
        }

        extendedFabAddExercises = view.findViewById(R.id.extended_fab_add_exercises)
        extendedFabAddExercises.setOnClickListener{
            // Navigate to SelectCategoryFragment
            val action = HomeFragmentDirections.actionHomeFragmentToSelectCategoryFragment()
            findNavController().navigate(action)
        }
    }
}