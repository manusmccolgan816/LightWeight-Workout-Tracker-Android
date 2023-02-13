package com.example.lightweight.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
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
    var workoutID: Int? = null

    // This stores the exercise instance IDs and a Boolean indicating whether the exercise instance
    // has been moved. This ensures that only the changed exercise instance numbers are updated in
    // onPause().
    private var exerciseInstanceIDisMoved: ArrayList<Pair<Int?, Boolean>> = arrayListOf()
    private var idNamePairs: List<IdNamePair> = listOf()

    private lateinit var textViewSelectedDate: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerViewExerciseInstances: RecyclerView
    private lateinit var buttonAddExercises: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the toolbar title
        val textViewToolbarTitle = activity?.findViewById(R.id.text_view_toolbar_title) as TextView
        textViewToolbarTitle.text = resources.getString(R.string.string_track_workouts)

        // Set the share icon to be visible
        val imageViewShareWorkout = activity?.findViewById(R.id.image_view_share_workout) as ImageView
        imageViewShareWorkout.visibility = View.VISIBLE

        // Set the select date icon to be visible
        val imageViewSelectDate = activity?.findViewById(R.id.image_view_select_date) as ImageView
        imageViewSelectDate.visibility = View.VISIBLE

        textViewSelectedDate = view.findViewById(R.id.text_view_selected_date)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerViewExerciseInstances = view.findViewById(R.id.recycler_view_exercise_instances)
        buttonAddExercises = view.findViewById(R.id.button_add_exercises)

        recyclerViewExerciseInstances.visibility = View.INVISIBLE

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        // If the parameter value is not the default...
        if (args.selectedDate != "today") {
            //...convert the argument to LocalDate type
            selectedDate = LocalDate.parse(args.selectedDate, formatter)
        }

        textViewSelectedDate.text = selectedDate.format(formatter)

        adapter = HomeParentWorkoutAdapter(
            fun() {
                Log.d(logTag, "recyclerViewPopulated called")
                // When the recycler view has been populated, display it and hide the progress bar
                progressBar.visibility = View.GONE
                recyclerViewExerciseInstances.visibility = View.VISIBLE
            },
            listOf(),
            this)
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
                Collections.swap(exerciseInstanceIDisMoved, sourcePos, targetPos)

                // The second item in exerciseInstanceIDisMoved is set to true to indicate that the
                // database values must be changed for exercise instances of the respective IDs
                exerciseInstanceIDisMoved[sourcePos] =
                    exerciseInstanceIDisMoved[sourcePos].copy(second = true)
                exerciseInstanceIDisMoved[targetPos] =
                    exerciseInstanceIDisMoved[targetPos].copy(second = true)

                adapter.notifyItemMoved(sourcePos, targetPos)

                Log.d(logTag, "sourcePos: $sourcePos, targetPos: $targetPos")

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
                        if (it.isNullOrEmpty()) {
                            progressBar.visibility = View.GONE
                        }

                        adapter.idNamePairs = it
                        idNamePairs = it

                        exerciseInstanceIDisMoved.clear()
                        // Populate the list with every exercise instance's ID and associated
                        // number used for ordering
                        for (i in it.indices) {
                            exerciseInstanceIDisMoved.add(Pair(it[i].id, false))
                        }

                        adapter.notifyDataSetChanged()
                        Log.d(logTag, "Got ${it.size} exercise instance IDs and exercise names")
                    }
            }
        }

        imageViewShareWorkout.setOnClickListener {
//            ShareWorkoutDialog(requireContext(), idNamePairs, this).show()
            val dialog = ShareWorkoutDialogFragment(idNamePairs, this)
            dialog.show(requireActivity().supportFragmentManager, "ShareWorkout")
        }

        imageViewSelectDate.setOnClickListener {
            // Navigate to CalendarFragment, passing the selected date
            val action = HomeFragmentDirections
                .actionHomeFragmentToCalendarFragment(selectedDate.toString())
            findNavController().navigate(action)
        }

        buttonAddExercises.setOnClickListener {
            // Navigate to SelectCategoryFragment
            val action = HomeFragmentDirections
                .actionHomeFragmentToSelectCategoryFragment(selectedDate.toString())
            findNavController().navigate(action)
        }
    }

    override fun onPause() {
        super.onPause()

        // Update the exercise instance numbers of those that were moved
        for (i in exerciseInstanceIDisMoved.indices) {
            if (exerciseInstanceIDisMoved[i].second) {
                exerciseInstanceViewModel.updateExerciseInstanceNumber(
                    exerciseInstanceIDisMoved[i].first,
                    i + 1
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d(logTag, parentFragment.toString())
    }
}