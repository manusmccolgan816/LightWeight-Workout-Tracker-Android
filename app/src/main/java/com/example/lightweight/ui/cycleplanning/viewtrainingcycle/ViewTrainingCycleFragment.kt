package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.CycleDayCategoryCombo
import com.example.lightweight.CycleDayCategoryExerciseCombo
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModelFactory
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY_CAT
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY_EX
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class ViewTrainingCycleFragment : Fragment(R.layout.fragment_view_training_cycle), KodeinAware {

    private val logTag = "ViewTrainingCycleFragment"

    override val kodein by kodein()
    private val cycleFactory: CycleViewModelFactory by instance()
    private val cycleDayFactory: CycleDayViewModelFactory by instance()
    private val cycleDayCategoryFactory: CycleDayCategoryViewModelFactory by instance()
    private val cycleDayExerciseFactory: CycleDayExerciseViewModelFactory by instance()

    private val cycleViewModel: CycleViewModel by viewModels { cycleFactory }
    private val cycleDayViewModel: CycleDayViewModel by viewModels { cycleDayFactory }
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by viewModels {
        cycleDayCategoryFactory
    }
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel by viewModels {
        cycleDayExerciseFactory
    }

    private val args: ViewTrainingCycleFragmentArgs by navArgs()

    private var cycleID: Int? = null

    private var items = arrayListOf<Pair<Int, Int?>>()
    private var cycleDayCatCombos = arrayListOf<CycleDayCategoryCombo>()
    private var cycleDayCatExCombos = arrayListOf<CycleDayCategoryExerciseCombo>()

    private lateinit var recyclerViewTrainingCycleDays: RecyclerView
    private lateinit var fabAddTrainingCycleDay: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cycleID = args.cycleID

        val ref = this.activity

        lifecycleScope.launch(Dispatchers.IO) {
            val cycle = cycleViewModel.getCycleOfID(cycleID)
            ref?.runOnUiThread {
                // Set the toolbar title
                val textViewToolbarTitle = requireActivity().findViewById<TextView>(R.id.text_view_toolbar_title)
                textViewToolbarTitle.text = cycle.cycleName
            }
        }

        // Remove the share icon
        val imageViewShareWorkout = activity?.findViewById(R.id.image_view_share_workout) as ImageView
        imageViewShareWorkout.visibility = View.GONE

        // Remove the select date icon
        val imageViewSelectDate = activity?.findViewById(R.id.image_view_select_date) as ImageView
        imageViewSelectDate.visibility = View.GONE

        recyclerViewTrainingCycleDays = view.findViewById(R.id.recycler_view_training_cycle_days)
        fabAddTrainingCycleDay = view.findViewById(R.id.fab_add_training_cycle_day)

        val cycleDayAdapter = TrainingCycleDayAdapter(
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            this
        )

        recyclerViewTrainingCycleDays.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingCycleDays.adapter = cycleDayAdapter

        cycleDayExerciseViewModel.getCycleDataOfCycleID(cycleID).observe(viewLifecycleOwner) {
            val cycleDays: ArrayList<CycleDay> = arrayListOf()
            val catCombos: ArrayList<CycleDayCategoryCombo> = arrayListOf()
            val exCombos: ArrayList<CycleDayCategoryExerciseCombo> = arrayListOf()

            cycleDayAdapter.items = arrayListOf()
            cycleDayAdapter.cycleDays = arrayListOf()
            cycleDayAdapter.idNamePairsCategory = arrayListOf()
            cycleDayAdapter.idNamePairsExercise = arrayListOf()

            // Populate cycleDay, cycleDayCat and cycleDayCatEx so that the adapter properties can
            // amended
            for (i in it) {
                val cycleDay = CycleDay(cycleID, i.cycle_day_name, i.cycle_day_number)
                cycleDay.cycleDayID = i.cycle_day_ID
                if (cycleDay !in cycleDays) {
                    cycleDays.add(cycleDay)
                }

                if (i.category_name != null) {
                    val cycleDayCat = CycleDayCategoryCombo(
                        i.cycle_day_category_ID,
                        i.category_name,
                        i.cycle_day_ID
                    )
                    if (cycleDayCat !in catCombos) {
                        catCombos.add(cycleDayCat)
                    }

                    if (i.exercise_name != null) {
                        val cycleDayCatEx = CycleDayCategoryExerciseCombo(
                            i.cycle_day_exercise_ID,
                            i.exercise_name,
                            i.cycle_day_category_ID
                        )
                        if (cycleDayCatEx !in exCombos) {
                            exCombos.add(cycleDayCatEx)
                        }
                    }
                }
            }

            for (cycleDay in cycleDays) {
                // Add the cycle day to the end of items
                cycleDayAdapter.items.add(Pair(LAYOUT_CYCLE_DAY, cycleDay.cycleDayID))
                cycleDayAdapter.cycleDays.add(cycleDay)

                for (catCombo in catCombos) {
                    if (catCombo.cycle_day_ID == cycleDay.cycleDayID) {
                        // Add the cycle day category to the end of items
                        cycleDayAdapter.items.add(Pair(LAYOUT_CYCLE_DAY_CAT, null))
                        cycleDayAdapter.idNamePairsCategory.add(
                            Pair(catCombo.cycle_day_category_ID, catCombo.category_name)
                        )

                        for (exCombo in exCombos) {
                            // Add the cycle day exercise to the end of items
                            if (exCombo.cycle_day_category_ID == catCombo.cycle_day_category_ID) {
                                cycleDayAdapter.items.add(Pair(LAYOUT_CYCLE_DAY_EX, null))
                                cycleDayAdapter.idNamePairsExercise.add(
                                    Pair(exCombo.cycle_day_exercise_ID, exCombo.exercise_name)
                                )
                            }
                        }
                    }
                }
            }

            cycleDayAdapter.notifyDataSetChanged()
        }

        fabAddTrainingCycleDay.setOnClickListener {
            AddTrainingCycleDayDialog(
                requireContext(),
                cycleID,
                cycleDayAdapter.cycleDays.size,
                fun(cycleDay: CycleDay) { cycleDayViewModel.insert(cycleDay) }
            ).show()
        }
    }
}