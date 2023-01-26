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
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
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

    private var cycleDaysAndCategories: ArrayList<ArrayList<CycleDay>> = ArrayList()

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
            listOf(),
            fun(category: Category) { this.category = category },
            this
        )
        val cycleDayCategoryAdapter = TrainingCycleDayCategoryAdapter(mapOf(), this)
        val concatAdapter = ConcatAdapter(cycleDayAdapter, cycleDayCategoryAdapter)

        recyclerViewTrainingCycleDays.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingCycleDays.adapter = concatAdapter

        cycleDayViewModel.getCycleDaysOfCycle(cycleID).observe(viewLifecycleOwner) { cycleDays ->
            cycleDayAdapter.cycleDays = cycleDays
            cycleDayAdapter.notifyDataSetChanged()
            Log.d(logTag, "cycleDayAdapter data set changed")

            for (cycleDay in cycleDays) {
                cycleDayCategoryViewModel.getCycleDayCategoriesAndNamesOfCycleDay(cycleDay.cycleDayID)
                    .observe(viewLifecycleOwner) { idNameMappings ->
                        cycleDayCategoryAdapter.idNameMappings = idNameMappings
                        cycleDayCategoryAdapter.notifyDataSetChanged()
                        Log.d(logTag, "cycleDayCategoryAdapter data set changed")
                    }
            }
        }

        fabAddTrainingCycleDay.setOnClickListener {
            AddTrainingCycleDayDialog(
                requireContext(),
                cycleID,
                cycleDayAdapter.itemCount,
                fun(cycleDay: CycleDay) { cycleDayViewModel.insert(cycleDay) }
            ).show()
        }
    }
}