package com.example.lightweight.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Exercise
import com.example.lightweight.ui.exercise.ExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModelFactory
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.workout.WorkoutViewModel
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(R.layout.fragment_home), KodeinAware {

    override val kodein by kodein()
    private val exerciseFactory: ExerciseViewModelFactory by instance()
    private val exerciseViewModel: ExerciseViewModel by viewModels { exerciseFactory }
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()

    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }

    private val args: HomeFragmentArgs by navArgs()

    private lateinit var adapter: HomeParentWorkoutAdapter

    // The selected date defaults to the current day
    private var selectedDate: LocalDate = LocalDate.now()
    private var workoutID: Int? = null

    private lateinit var textViewSelectedDate: TextView
    private lateinit var recyclerViewExerciseInstances: RecyclerView
    private lateinit var fabCalendar: FloatingActionButton
    private lateinit var extendedFabAddExercises: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the action bar title
        activity?.title = "Track Workouts"

        textViewSelectedDate = view.findViewById(R.id.text_view_selected_date)
        recyclerViewExerciseInstances = view.findViewById(R.id.recycler_view_exercise_instances)
        fabCalendar = view.findViewById(R.id.fab_calendar)
        extendedFabAddExercises = view.findViewById(R.id.extended_fab_add_exercises)

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        // If the parameter value is not the default...
        if (args.selectedDate != "today") {
            //...convert the argument to LocalDate type
            selectedDate = LocalDate.parse(args.selectedDate, formatter)
        }

        textViewSelectedDate.text = selectedDate.format(formatter)

        val ref = this.activity
        lifecycleScope.launch(Dispatchers.IO) {
            workoutID = workoutViewModel.getWorkoutOfDate(selectedDate.toString())?.workoutID

            ref?.runOnUiThread {
                exerciseInstanceViewModel.getExerciseInstancesOfWorkout(workoutID)
                    .observe(viewLifecycleOwner) {
                        adapter.exerciseInstances = it
//                        adapter.notifyDataSetChanged()
                        for (i in it.indices) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val exerciseName = exerciseViewModel.getExerciseOfID(it[i].exerciseID).exerciseName
                                ref.runOnUiThread {
                                    adapter.exerciseNames.add(exerciseName)
                                    if (i == it.size - 1) {
                                        Log.d(null, "Notifying HomeParentWorkoutAdapter data set change")
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }
            }
        }

        adapter = HomeParentWorkoutAdapter(listOf(), arrayListOf(), this)
        recyclerViewExerciseInstances.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExerciseInstances.adapter = adapter

        fabCalendar.setOnClickListener {
            // Navigate to CalendarFragment, passing the selected date
            val action = HomeFragmentDirections
                .actionHomeFragmentToCalendarFragment(selectedDate.toString())
            findNavController().navigate(action)
        }

        extendedFabAddExercises.setOnClickListener{
            // Navigate to SelectCategoryFragment
            val action = HomeFragmentDirections
                .actionHomeFragmentToSelectCategoryFragment(selectedDate.toString())
            findNavController().navigate(action)
        }
    }
}