package com.example.lightweight.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lightweight.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var extendedFabAddExercises: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        extendedFabAddExercises = view.findViewById(R.id.extended_fab_add_exercises)
        extendedFabAddExercises.setOnClickListener{
            // Navigate to SelectCategoryFragment
            val action = HomeFragmentDirections.actionHomeFragmentToSelectCategoryFragment()
            findNavController().navigate(action)
        }
    }
}