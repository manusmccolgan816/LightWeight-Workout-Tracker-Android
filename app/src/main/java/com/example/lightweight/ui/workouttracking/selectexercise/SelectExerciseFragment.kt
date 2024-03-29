package com.example.lightweight.ui.workouttracking.selectexercise

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.ui.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectExerciseFragment(
    private val viewModel: SelectExerciseViewModel
) : Fragment(R.layout.fragment_select_exercise) {

    private val args: SelectExerciseFragmentArgs by navArgs()

    private lateinit var searchViewExercises: SearchView
    private lateinit var recyclerViewExercises: RecyclerView
    private lateinit var fabAddExercise: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ExerciseItemAdapter(args.selectedDate, listOf(), viewModel, this)

        val categoryID = args.categoryID
        var exercises: List<Exercise> = listOf()

        searchViewExercises = view.findViewById(R.id.search_view_exercises)
        recyclerViewExercises = view.findViewById(R.id.recycler_view_exercises)
        fabAddExercise = view.findViewById(R.id.fab_add_exercise)

        val ref = this.activity

        if (activity!!::class == MainActivity::class) {
            lifecycleScope.launch(Dispatchers.IO) {
                val category = viewModel.getCategoryOfID(categoryID)
                ref?.runOnUiThread {
                    // Set the toolbar title
                    val textViewToolbarTitle =
                        requireActivity().findViewById<TextView>(R.id.text_view_toolbar_title)
                    textViewToolbarTitle.text = category.categoryName
                }
            }

            // Remove the share icon
            val imageViewShareWorkout =
                activity?.findViewById(R.id.image_view_share_workout) as ImageView
            imageViewShareWorkout.visibility = View.GONE

            // Remove the select date icon
            val imageViewSelectDate = activity?.findViewById(R.id.image_view_select_date) as ImageView
            imageViewSelectDate.visibility = View.GONE
        }

        recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExercises.adapter = adapter

        viewModel.getExercisesOfCategory(categoryID).observe(viewLifecycleOwner) {
            exercises = it
            adapter.exercises = it
            adapter.notifyDataSetChanged()
        }

        searchViewExercises.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // If text has been input...
                if (newText != null) {
                    val filteredList: ArrayList<Exercise> = ArrayList()

                    for (exercise: Exercise in exercises) {
                        if (exercise.exerciseName.lowercase().contains(newText.lowercase())) {
                            filteredList.add(exercise)
                        }
                    }

                    adapter.exercises = filteredList
                    adapter.notifyDataSetChanged()
                }

                return true
            }
        })

        fabAddExercise.setOnClickListener {
            // Use a coroutine to execute the query and alter editTextDiaryEntry accordingly
            lifecycleScope.launch(Dispatchers.IO) {
                val category = viewModel.getCategoryOfID(categoryID)
                ref?.runOnUiThread {
                    // Display the add category dialog
                    AddExerciseDialog(
                        requireContext(),
                        category,
                        fun(exercise: Exercise) {
                            viewModel.insertExercise(exercise)
                            // Clear the search view text
                            searchViewExercises.setQuery("", false)
                            // Ensure the keyboard does not appear
                            searchViewExercises.clearFocus()
                        }
                    ).show()
                }
            }
        }
    }
}