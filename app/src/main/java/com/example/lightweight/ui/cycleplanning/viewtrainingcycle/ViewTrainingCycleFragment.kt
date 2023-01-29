package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
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
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY_CAT
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

    private val cycleViewModel: CycleViewModel by viewModels { cycleFactory }
    private val cycleDayViewModel: CycleDayViewModel by viewModels { cycleDayFactory }
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by viewModels {
        cycleDayCategoryFactory
    }

    private val args: ViewTrainingCycleFragmentArgs by navArgs()

    private var cycleID: Int? = null

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

        val cycleDayAdapter = TrainingCycleDayAdapter(
            arrayListOf(),
            listOf(),
            arrayListOf(),
            this
        )

        recyclerViewTrainingCycleDays.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingCycleDays.adapter = cycleDayAdapter

        cycleDayCategoryViewModel.getCycleDayAndCycleDayCategoriesOfCycle(cycleID)
            .observe(viewLifecycleOwner) {
                if (it.isEmpty()) return@observe

                val cycleDays: ArrayList<CycleDay> = arrayListOf()
                val cycleDayCatCombos: ArrayList<CycleDayCategoryCombo> = arrayListOf()

                cycleDayAdapter.items = arrayListOf()

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
                        if (cycleDayCat !in cycleDayCatCombos) {
                            cycleDayCatCombos.add(cycleDayCat)
                        }
                    }
                }

                for (i in cycleDays.indices) {
                    cycleDayAdapter.items.add(Pair(LAYOUT_CYCLE_DAY, cycleDays[i].cycleDayID))
//                    if (cycleDayAdapter.items.size - combos.size <= i) {
//                        cycleDayAdapter.items.add(Pair(LAYOUT_CYCLE_DAY, cycleDays[i].cycleDayID))
//                    }
                }
                items = cycleDayAdapter.items
                cycleDayAdapter.cycleDays = cycleDays

                combos = arrayListOf()

                for (cycleDay in cycleDays) {
                    for (combo in cycleDayCatCombos) {
                        if (combo !in combos) {
                            Loop@ for (i in 0 until cycleDayAdapter.items.size) {
                                if (cycleDayAdapter.items[i].second == combo.cycle_day_ID) {
                                    var sameDayCatCount = 0
                                    for (j in combos.indices) {
                                        if (combos[j].cycle_day_ID == combo.cycle_day_ID) {
                                            sameDayCatCount++
                                        }
                                    }
                                    if (i + 1 >= cycleDayAdapter.items.size) {
                                        cycleDayAdapter.items.add(
                                            Pair(
                                                LAYOUT_CYCLE_DAY_CAT,
                                                null
                                            )
                                        )
                                        //cycleDayAdapter1.notifyItemInserted(cycleDayAdapter1.itemCount - 1)
                                    } else {
                                        cycleDayAdapter.items.add(
                                            i + sameDayCatCount + 1,
                                            Pair(LAYOUT_CYCLE_DAY_CAT, null)
                                        )
                                        //cycleDayAdapter1.notifyItemInserted(i + sameDayCatCount + 1)
                                    }
                                    combos.add(combo)
                                    break@Loop
                                }
                            }
                        }
                    }
                    cycleDayAdapter.idNamePairs = arrayListOf()
                    for (i in combos) {
                        cycleDayAdapter.idNamePairs.add(
                            Pair(
                                i.cycle_day_category_ID,
                                i.category_name
                            )
                        )
                    }
                    cycleDayAdapter.notifyDataSetChanged()
                }
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