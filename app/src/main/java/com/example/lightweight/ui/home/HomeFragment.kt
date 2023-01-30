package com.example.lightweight.ui.home

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.WrapContentLinearLayoutManager
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
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home), KodeinAware {

    private val logTag = "HomeFragment"

    override val kodein by kodein()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()

    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by viewModels {
        exerciseInstanceFactory
    }

    private val args: HomeFragmentArgs by navArgs()

    private lateinit var adapter: HomeParentWorkoutAdapter

    // The selected date defaults to the current day
    var selectedDate: LocalDate = LocalDate.now()
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

        adapter = HomeParentWorkoutAdapter(listOf(), this)
        recyclerViewExerciseInstances.layoutManager =
            WrapContentLinearLayoutManager(requireContext())
        recyclerViewExerciseInstances.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePos = source.absoluteAdapterPosition
                val targetPos = target.absoluteAdapterPosition

                // Swap the positions of the two exercise instances
                Collections.swap(adapter.idNamePairs, sourcePos, targetPos)
                adapter.notifyItemMoved(sourcePos, targetPos)
                Log.d(logTag, "sourcePos: $sourcePos, targetPos: $targetPos")

                // Update the exercise instance numbers of the two exercise instances to reflect
                // the swap
                val sourceID = adapter.idNamePairs[sourcePos].id
                val targetID = adapter.idNamePairs[targetPos].id
//                exerciseInstanceViewModel.updateExerciseInstanceNumber(sourceID, targetPos + 1)
//                exerciseInstanceViewModel.updateExerciseInstanceNumber(targetID, sourcePos + 1)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerViewExerciseInstances)

        val ref = this.activity
        lifecycleScope.launch(Dispatchers.IO) {
            workoutID = workoutViewModel.getWorkoutOfDate(selectedDate.toString())?.workoutID

            ref?.runOnUiThread {
                exerciseInstanceViewModel.getExerciseInstancesAndNamesOfWorkout(workoutID)
                    .observe(viewLifecycleOwner) {
                        adapter.idNamePairs = it
                        adapter.notifyDataSetChanged()
                        Log.d(logTag, "Got ${it.size} exercise instance IDs and exercise names")
                    }
            }
        }

        fabCalendar.setOnClickListener {
            // Navigate to CalendarFragment, passing the selected date
            val action = HomeFragmentDirections
                .actionHomeFragmentToCalendarFragment(selectedDate.toString())
            findNavController().navigate(action)
        }

        extendedFabAddExercises.setOnClickListener {
            // Navigate to SelectCategoryFragment
            val action = HomeFragmentDirections
                .actionHomeFragmentToSelectCategoryFragment(selectedDate.toString())
            findNavController().navigate(action)
        }
    }
}