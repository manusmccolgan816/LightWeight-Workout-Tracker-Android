package com.example.lightweight.ui.exercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.category.CategoryViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectExerciseFragment : Fragment(R.layout.fragment_select_exercise), KodeinAware {

    override val kodein by kodein()
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val categoryFactory: CategoryViewModelFactory by instance()

    private val args: SelectExerciseFragmentArgs by navArgs()

    private lateinit var recyclerViewExercises: RecyclerView
    private lateinit var fabAddExercise: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseViewModel: ExerciseViewModel by viewModels { exerciseFactory }
        val categoryViewModel: CategoryViewModel by viewModels { categoryFactory }
        val adapter = ExerciseItemAdapter(args.selectedDate, listOf(), exerciseViewModel, this)

        val categoryID = args.categoryID

        recyclerViewExercises = view.findViewById(R.id.recycler_view_exercises)
        recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExercises.adapter = adapter

        exerciseViewModel.getExercisesOfCategory(categoryID).observe(viewLifecycleOwner) {
            adapter.exercises = it
            adapter.notifyDataSetChanged()
        }

        fabAddExercise = view.findViewById(R.id.fab_add_exercise)
        fabAddExercise.setOnClickListener {
            val ref = this.activity
            // Use a coroutine to execute the query and alter editTextDiaryEntry accordingly
            lifecycleScope.launch(Dispatchers.IO) {
                val category = categoryViewModel.getCategoryOfID(categoryID)
                ref?.runOnUiThread {
                    // Display the add category dialog
                    AddExerciseDialog(
                        requireContext(),
                        category,
                        fun(exercise: Exercise) { exerciseViewModel.insert(exercise) }
                    ).show()
                }
            }

        }
    }

}