package com.example.lightweight.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lightweight.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val args: HomeFragmentArgs by navArgs()

    // The selected date defaults to the current day
    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var textViewSelectedDate: TextView
    private lateinit var fabCalendar: FloatingActionButton
    private lateinit var extendedFabAddExercises: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewSelectedDate = view.findViewById(R.id.text_view_selected_date)
        fabCalendar = view.findViewById(R.id.fab_calendar)
        extendedFabAddExercises = view.findViewById(R.id.extended_fab_add_exercises)

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        // If the parameter value is not the default...
        if (args.selectedDate != "today") {
            //...convert the argument to LocalDate type
            selectedDate = LocalDate.parse(args.selectedDate, formatter)
        }

        textViewSelectedDate.text = selectedDate.format(formatter)

        fabCalendar.setOnClickListener {
            // Navigate to CalendarFragment, passing the selected date
            val action = HomeFragmentDirections
                .actionHomeFragmentToCalendarFragment(selectedDate.toString())
            findNavController().navigate(action)
        }

        extendedFabAddExercises.setOnClickListener{
            // Navigate to SelectCategoryFragment
            val action = HomeFragmentDirections.actionHomeFragmentToSelectCategoryFragment()
            findNavController().navigate(action)
        }
    }
}