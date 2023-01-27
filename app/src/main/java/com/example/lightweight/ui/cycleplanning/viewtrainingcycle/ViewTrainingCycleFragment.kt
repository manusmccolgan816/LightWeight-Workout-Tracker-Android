package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.CycleDayCategoryCombo
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter1.Companion.LAYOUT_CYCLE_DAY
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter1.Companion.LAYOUT_CYCLE_DAY_CAT
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ViewTrainingCycleFragment : Fragment(R.layout.fragment_view_training_cycle), KodeinAware {

    private val logTag = "ViewTrainingCycleFragment"

    override val kodein by kodein()
    private val cycleFactory: CycleViewModelFactory by instance()
    private val cycleDayFactory: CycleDayViewModelFactory by instance()
    private val cycleDayCategoryFactory: CycleDayCategoryViewModelFactory by instance()

    private val cycleViewModel: CycleViewModel by viewModels { cycleFactory }
    private val cycleDayViewModel: CycleDayViewModel by viewModels { cycleDayFactory }
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by viewModels {
        cycleDayCategoryFactory
    }

    private val args: ViewTrainingCycleFragmentArgs by navArgs()

    private var cycleID: Int? = null
    private lateinit var category: Category

    private var items = arrayListOf<Pair<Int, Int?>>()
    private var combos = arrayListOf<CycleDayCategoryCombo>()

    private lateinit var recyclerViewTrainingCycleDays: RecyclerView
    private lateinit var fabAddTrainingCycleDay: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cycleID = args.cycleID

        val ref = this.activity

        lifecycleScope.launch(Dispatchers.IO) {
            val cycle = cycleViewModel.getCycleOfID(cycleID)
            ref?.runOnUiThread {
                // Set the action bar title
                activity?.title = cycle.cycleName
            }
        }

        recyclerViewTrainingCycleDays = view.findViewById(R.id.recycler_view_training_cycle_days)
        fabAddTrainingCycleDay = view.findViewById(R.id.fab_add_training_cycle_day)

//        val cycleDayAdapter = TrainingCycleDayAdapter(
//            listOf(),
//            fun(category: Category) { this.category = category },
//            this
//        )
        val cycleDayAdapter1 = TrainingCycleDayAdapter1(
            arrayListOf(),
            listOf(),
            fun(category: Category) { this.category = category },
            arrayListOf(),
            this
        )

        recyclerViewTrainingCycleDays.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingCycleDays.adapter = cycleDayAdapter1
//        recyclerViewTrainingCycleDays.adapter = concatAdapter

        cycleDayViewModel.getCycleDaysOfCycle(cycleID).observe(viewLifecycleOwner) { cycleDays ->
            if (cycleDays.isEmpty()) return@observe

            for (i in cycleDays.indices) {
                if (cycleDayAdapter1.items.size - combos.size <= i) {
                    cycleDayAdapter1.items.add(Pair(LAYOUT_CYCLE_DAY, cycleDays[i].cycleDayID))
                    //cycleDayAdapter1.notifyItemInserted(cycleDayAdapter1.itemCount - 1)
                    Log.d(logTag, "items.add: 0")
                    //cycleDayAdapter1.notifyItemInserted(cycleDayAdapter1.items.size + i)
                }
            }
            items = cycleDayAdapter1.items
            cycleDayAdapter1.cycleDays = cycleDays
            cycleDayAdapter1.notifyDataSetChanged()
            Log.d(logTag, "cycleDayAdapter1 data set changed")

            for (cycleDay in cycleDays) {
                cycleDayCategoryViewModel.getCycleDayCategoriesNamesCycleDaysOfCycleDay(cycleDay.cycleDayID)
                    .observe(viewLifecycleOwner) { cycleDayCatCombos ->
                        if (cycleDayCatCombos.isNotEmpty()) {
                            for (combo in cycleDayCatCombos) {
                                if (combo !in combos) {
                                    Loop@ for (i in 0 until cycleDayAdapter1.items.size) {
                                        if (cycleDayAdapter1.items[i].second == combo.cycle_day_ID) {
                                            var sameDayCatCount = 0
                                            for (j in combos.indices) {
                                                if (combos[j].cycle_day_ID == combo.cycle_day_ID) {
                                                    sameDayCatCount++
                                                }
                                            }
//                                            // If the item in this position is a category
//                                            if (cycleDayAdapter1.items[i].first == LAYOUT_CYCLE_DAY_CAT) {
//                                                // Ensure it is added to the correct position
//                                            }

                                            if (i + 1 >= cycleDayAdapter1.items.size) {
                                                cycleDayAdapter1.items.add(
                                                    Pair(
                                                        LAYOUT_CYCLE_DAY_CAT,
                                                        null
                                                    )
                                                )
                                                cycleDayAdapter1.notifyItemInserted(cycleDayAdapter1.itemCount - 1)
                                            } else {
                                                cycleDayAdapter1.items.add(
                                                    i + sameDayCatCount + 1,
                                                    Pair(LAYOUT_CYCLE_DAY_CAT, null)
                                                )
                                                cycleDayAdapter1.notifyItemInserted(i + sameDayCatCount + 1)
                                            }
                                            combos.add(combo)
                                            break@Loop
                                        }
                                    }
                                }
                            }

                            //items = cycleDayAdapter1.items
                            cycleDayAdapter1.idNamePairs = arrayListOf()
                            for (i in combos) {
                                cycleDayAdapter1.idNamePairs.add(
                                    Pair(
                                        i.cycle_day_category_ID,
                                        i.category_name
                                    )
                                )
                            }

                            //cycleDayAdapter1.notifyDataSetChanged()

                            Log.d(
                                logTag,
                                "cda1 data set changed, idNamePairs size: ${cycleDayAdapter1.idNamePairs.size}"
                            )
                        }
                    }
            }
        }

        fabAddTrainingCycleDay.setOnClickListener {
            AddTrainingCycleDayDialog(
                requireContext(),
                cycleID,
                cycleDayAdapter1.itemCount, // TODO Ensure this is changed
                fun(cycleDay: CycleDay) { cycleDayViewModel.insert(cycleDay) }
            ).show()
        }
    }
}