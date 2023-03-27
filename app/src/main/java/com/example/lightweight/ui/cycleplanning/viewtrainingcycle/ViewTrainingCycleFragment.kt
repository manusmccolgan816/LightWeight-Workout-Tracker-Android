package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryCombo
import com.example.lightweight.ui.cycleplanning.CycleDayCategoryExerciseCombo
import com.example.lightweight.ui.cycleplanning.cycle.CycleViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY_CAT
import com.example.lightweight.ui.cycleplanning.viewtrainingcycle.TrainingCycleDayAdapter.Companion.LAYOUT_CYCLE_DAY_EX
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewTrainingCycleFragment(
    private val cycleViewModel: CycleViewModel,
    private val cycleDayViewModel: CycleDayViewModel,
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel
) : Fragment(R.layout.fragment_view_training_cycle) {

    private val logTag = "ViewTrainingCycleFragment"

    private val args: ViewTrainingCycleFragmentArgs by navArgs()

    private var cycleID: Int? = null
    private var numItems = 0

    private lateinit var cycleDayAdapter: TrainingCycleDayAdapter

    private lateinit var recyclerViewTrainingCycleDays: RecyclerView
    private lateinit var fabAddTrainingCycleDay: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cycleID = args.cycleID

        if (this.activity is MainActivity) {
            val ref = this.activity
            lifecycleScope.launch(Dispatchers.IO) {
                val cycleName = cycleViewModel.getCycleOfID(cycleID).cycleName
                ref?.runOnUiThread {
                    // Set the toolbar title
                    val textViewToolbarTitle =
                        requireActivity().findViewById<TextView>(R.id.text_view_toolbar_title)
                    textViewToolbarTitle.text = cycleName
                }
            }

            // Set the share icon to be visible
            val imageViewShareWorkout =
                activity?.findViewById(R.id.image_view_share_workout) as ImageView
            imageViewShareWorkout.visibility = View.VISIBLE

            // Remove the select date icon
            val imageViewSelectDate =
                activity?.findViewById(R.id.image_view_select_date) as ImageView
            imageViewSelectDate.visibility = View.GONE

            imageViewShareWorkout.setOnClickListener {
                // If there are no cycle items (cycle days, categories or exercises)...
                if (cycleDayAdapter.items.isEmpty()) {
                    Toast.makeText(
                        context,
                        "There is nothing to share in this training cycle",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                val dialog = ShareTrainingCycleDialogFragment(cycleDayAdapter, cycleID, this)
                dialog.show(requireActivity().supportFragmentManager, "ShareTrainingCycle")
            }
        }


        recyclerViewTrainingCycleDays = view.findViewById(R.id.recycler_view_training_cycle_days)
        fabAddTrainingCycleDay = view.findViewById(R.id.fab_add_training_cycle_day)

        cycleDayAdapter = TrainingCycleDayAdapter(
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            this
        )

        recyclerViewTrainingCycleDays.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingCycleDays.adapter = cycleDayAdapter

        cycleDayExerciseViewModel.getCycleItemsOfCycleID(cycleID)
            .observe(viewLifecycleOwner) { cycleItems ->
                Log.d(logTag, "Entered cycle items observer")

                val cycleDays: ArrayList<CycleDay> = arrayListOf()
                val catCombos: ArrayList<CycleDayCategoryCombo> = arrayListOf()
                val exCombos: ArrayList<CycleDayCategoryExerciseCombo> = arrayListOf()
                val items: ArrayList<Pair<Int, Int?>> = arrayListOf()

                // Populate cycleDay, cycleDayCat and cycleDayCatEx so that the adapter properties
                // can be amended
                for (cycleItem in cycleItems) {
                    val cycleDay =
                        CycleDay(cycleID, cycleItem.cycle_day_name, cycleItem.cycle_day_number)
                    cycleDay.cycleDayID = cycleItem.cycle_day_ID
                    if (cycleDay !in cycleDays) {
                        cycleDays.add(cycleDay)
                        items.add(Pair(LAYOUT_CYCLE_DAY, cycleDay.cycleDayID))
                    }

                    if (cycleItem.category_name != null) {
                        val cycleDayCat = CycleDayCategoryCombo(
                            cycleItem.cycle_day_category_ID,
                            cycleItem.category_name,
                            cycleItem.cycle_day_ID
                        )
                        if (cycleDayCat !in catCombos) {
                            catCombos.add(cycleDayCat)
                            items.add(Pair(LAYOUT_CYCLE_DAY_CAT, cycleItem.cycle_day_category_ID))
                        }

                        if (cycleItem.exercise_name != null) {
                            val cycleDayCatEx = CycleDayCategoryExerciseCombo(
                                cycleItem.cycle_day_exercise_ID,
                                cycleItem.exercise_name,
                                cycleItem.cycle_day_category_ID
                            )
                            if (cycleDayCatEx !in exCombos) {
                                exCombos.add(cycleDayCatEx)
                                items.add(
                                    Pair(
                                        LAYOUT_CYCLE_DAY_EX,
                                        cycleItem.cycle_day_exercise_ID
                                    )
                                )
                            }
                        }
                    }
                }

                Log.d(logTag, "numItems: $numItems")
                // If no item has been added or removed, do not continue
                if (numItems == items.size && cycleDays == cycleDayAdapter.cycleDays) {
                    return@observe
                }
                numItems = items.size

                // If an item has been added
                if (items.size == cycleDayAdapter.itemCount + 1) {
                    // If the added item was a cycle day
                    if (cycleDays.size != cycleDayAdapter.cycleDays.size) {
                        for (i in cycleDays.indices) {
                            // If this is the item that was added
                            if (i >= cycleDayAdapter.cycleDays.size ||
                                cycleDays[i].cycleDayID != cycleDayAdapter.cycleDays[i].cycleDayID
                            ) {
                                for (j in items.indices) {
                                    // If this is the position that the item was added
                                    if (items[j].first == LAYOUT_CYCLE_DAY &&
                                        items[j].second == cycleDays[i].cycleDayID
                                    ) {
                                        // Update the adapter to include this item
                                        cycleDayAdapter.items.add(
                                            j,
                                            Pair(LAYOUT_CYCLE_DAY, items[j].second)
                                        )
                                        cycleDayAdapter.cycleDays.add(i, cycleDays[i])
                                        cycleDayAdapter.notifyItemInserted(j)
                                    }
                                }
                            }
                        }
                    }
                    // If the added item was a category
                    else if (catCombos.size != cycleDayAdapter.idNamePairsCategory.size) {
                        for (i in catCombos.indices) {
                            // If this is the item that was added
                            if (i >= cycleDayAdapter.idNamePairsCategory.size ||
                                catCombos[i].cycle_day_category_ID !=
                                cycleDayAdapter.idNamePairsCategory[i].first
                            ) {
                                for (j in items.indices) {
                                    // If this is the position that the item was added
                                    if (items[j].first == LAYOUT_CYCLE_DAY_CAT &&
                                        items[j].second == catCombos[i].cycle_day_category_ID
                                    ) {
                                        // Update the adapter to include this item
                                        cycleDayAdapter.items.add(
                                            j,
                                            Pair(
                                                LAYOUT_CYCLE_DAY_CAT,
                                                catCombos[i].cycle_day_category_ID
                                            )
                                        )
                                        cycleDayAdapter.idNamePairsCategory.add(
                                            i,
                                            Pair(
                                                catCombos[i].cycle_day_category_ID,
                                                catCombos[i].category_name
                                            )
                                        )
                                        cycleDayAdapter.notifyItemInserted(j)
                                    }
                                }
                            }
                        }
                    }
                    // If the added item was an exercise
                    else if (exCombos.size != cycleDayAdapter.idNamePairsExercise.size) {
                        for (i in exCombos.indices) {
                            // If this is the item that was added
                            if (i >= cycleDayAdapter.idNamePairsExercise.size ||
                                exCombos[i].cycle_day_exercise_ID !=
                                cycleDayAdapter.idNamePairsExercise[i].first
                            ) {
                                for (j in items.indices) {
                                    // If this is the position that the item was added
                                    if (items[j].first == LAYOUT_CYCLE_DAY_EX &&
                                        items[j].second == exCombos[i].cycle_day_exercise_ID
                                    ) {
                                        // Update the adapter to include this item
                                        cycleDayAdapter.items.add(
                                            j,
                                            Pair(
                                                LAYOUT_CYCLE_DAY_EX,
                                                exCombos[i].cycle_day_exercise_ID
                                            )
                                        )
                                        cycleDayAdapter.idNamePairsExercise.add(
                                            i,
                                            Pair(
                                                exCombos[i].cycle_day_exercise_ID,
                                                exCombos[i].exercise_name
                                            )
                                        )
                                        cycleDayAdapter.notifyItemInserted(j)
                                    }
                                }
                            }
                        }
                    }
                }
                // If an item has been removed
                else if (items.size < cycleDayAdapter.itemCount) {
                    // If the removed item was a cycle day
                    if (cycleDays.size != cycleDayAdapter.cycleDays.size) {
                        for (j in cycleDayAdapter.items.indices) {
                            val adapterItemType = cycleDayAdapter.items[j].first
                            // If this is where the item was removed
                            if (j >= items.size ||
                                (adapterItemType == LAYOUT_CYCLE_DAY &&
                                        (items[j].first != LAYOUT_CYCLE_DAY ||
                                                items[j].second != cycleDayAdapter.items[j].second))
                            ) {
                                // Remove the item from adapter's cycleDays
                                loop@ for (i in cycleDayAdapter.cycleDays.indices) {
                                    if (cycleDayAdapter.cycleDays[i].cycleDayID ==
                                        cycleDayAdapter.items[j].second
                                    ) {
                                        cycleDayAdapter.cycleDays.removeAt(i)
                                        break@loop
                                    }
                                }
                                // Remove the item from adapter's items
                                cycleDayAdapter.items.removeAt(j)
                                cycleDayAdapter.notifyItemRemoved(j)

                                Log.d(logTag, "cycleDay removed at position $j")

                                // Delete the cycle day's categories and exercises
                                while (j < cycleDayAdapter.items.size) {
                                    // No more items will be deleted if this is a cycle day
                                    if (cycleDayAdapter.items[j].first == LAYOUT_CYCLE_DAY) {
                                        return@observe
                                    }
                                    if (cycleDayAdapter.items[j].first == LAYOUT_CYCLE_DAY_CAT) {
                                        // Remove the item from adapter's idNamePairsCategory
                                        loop@ for (i in cycleDayAdapter.idNamePairsCategory.indices) {
                                            if (cycleDayAdapter.idNamePairsCategory[i].first
                                                == cycleDayAdapter.items[j].second
                                            ) {
                                                cycleDayAdapter.idNamePairsCategory.removeAt(i)
                                                break@loop
                                            }
                                        }
                                    }
                                    if (cycleDayAdapter.items[j].first == LAYOUT_CYCLE_DAY_EX) {
                                        // Remove the item from adapter's idNamePairsExercise
                                        loop@ for (i in cycleDayAdapter.idNamePairsExercise.indices) {
                                            if (cycleDayAdapter.idNamePairsExercise[i].first
                                                == cycleDayAdapter.items[j].second
                                            ) {
                                                cycleDayAdapter.idNamePairsExercise.removeAt(i)
                                                break@loop
                                            }
                                        }
                                    }
                                    // Remove the item from adapter's items
                                    cycleDayAdapter.items.removeAt(j)
                                    cycleDayAdapter.notifyItemRemoved(j)
                                }
                                return@observe
                            }
                        }
                    }
                    // If the removed item was a category
                    else if (catCombos.size != cycleDayAdapter.idNamePairsCategory.size) {
                        for (j in cycleDayAdapter.items.indices) {
                            val adapterItemType = cycleDayAdapter.items[j].first
                            // If this is where the item was removed
                            if (j >= items.size ||
                                (adapterItemType == LAYOUT_CYCLE_DAY_CAT &&
                                        (items[j].first != LAYOUT_CYCLE_DAY_CAT ||
                                                items[j].second != cycleDayAdapter.items[j].second))
                            ) {
                                // Remove the item from adapter's idNamePairsCategory
                                loop@ for (i in cycleDayAdapter.idNamePairsCategory.indices) {
                                    if (cycleDayAdapter.idNamePairsCategory[i].first ==
                                        cycleDayAdapter.items[j].second
                                    ) {
                                        cycleDayAdapter.idNamePairsCategory.removeAt(i)
                                        break@loop
                                    }
                                }
                                // Remove the item from adapter's items
                                cycleDayAdapter.items.removeAt(j)
                                cycleDayAdapter.notifyItemRemoved(j)

                                Log.d(logTag, "cycleDayCategory removed at position $j")

                                // Delete the category's exercises
                                while (j < cycleDayAdapter.items.size) {
                                    // No more items will be deleted if this is not an exercise
                                    if (cycleDayAdapter.items[j].first != LAYOUT_CYCLE_DAY_EX) {
                                        return@observe
                                    }
                                    // Remove the item from adapter's idNamePairsExercise
                                    loop@ for (i in cycleDayAdapter.idNamePairsExercise.indices) {
                                        if (cycleDayAdapter.idNamePairsExercise[i].first ==
                                            cycleDayAdapter.items[j].second
                                        ) {
                                            cycleDayAdapter.idNamePairsExercise.removeAt(i)
                                            break@loop
                                        }
                                    }
                                    // Remove the item from adapter's items
                                    cycleDayAdapter.items.removeAt(j)
                                    cycleDayAdapter.notifyItemRemoved(j)
                                }
                                return@observe
                            }
                        }
                    }
                    // If the removed item was an exercise
                    else if (exCombos.size != cycleDayAdapter.idNamePairsExercise.size) {
                        for (j in cycleDayAdapter.items.indices) {
                            val adapterItemType = cycleDayAdapter.items[j].first
                            // If this is where the item was removed
                            if (j >= items.size ||
                                (adapterItemType == LAYOUT_CYCLE_DAY_EX &&
                                        (items[j].first != LAYOUT_CYCLE_DAY_EX ||
                                                items[j].second != cycleDayAdapter.items[j].second))
                            ) {
                                // Remove the item from adapter's idNamePairsExercise
                                loop@ for (i in cycleDayAdapter.idNamePairsExercise.indices) {
                                    if (cycleDayAdapter.idNamePairsExercise[i].first ==
                                        cycleDayAdapter.items[j].second
                                    ) {
                                        cycleDayAdapter.idNamePairsExercise.removeAt(i)
                                        break@loop
                                    }
                                }
                                // Remove the item from the adapter's items
                                cycleDayAdapter.items.removeAt(j)
                                cycleDayAdapter.notifyItemRemoved(j)

                                Log.d(logTag, "cycleDayExercise removed at position $j")

                                return@observe
                            }
                        }
                    }
                } else {
                    Log.d(logTag, "updating adapter from scratch")

                    cycleDayAdapter.items = arrayListOf()
                    cycleDayAdapter.cycleDays = arrayListOf()
                    cycleDayAdapter.idNamePairsCategory = arrayListOf()
                    cycleDayAdapter.idNamePairsExercise = arrayListOf()

                    for (cycleDay in cycleDays) {
                        // Add the cycle day to the end of items
                        cycleDayAdapter.items.add(Pair(LAYOUT_CYCLE_DAY, cycleDay.cycleDayID))
                        cycleDayAdapter.cycleDays.add(cycleDay)

                        for (catCombo in catCombos) {
                            if (catCombo.cycle_day_ID == cycleDay.cycleDayID) {
                                // Add the cycle day category to the end of items
                                cycleDayAdapter.items.add(
                                    Pair(
                                        LAYOUT_CYCLE_DAY_CAT,
                                        catCombo.cycle_day_category_ID
                                    )
                                )
                                cycleDayAdapter.idNamePairsCategory.add(
                                    Pair(catCombo.cycle_day_category_ID, catCombo.category_name)
                                )

                                for (exCombo in exCombos) {
                                    // Add the cycle day exercise to the end of items
                                    if (exCombo.cycle_day_category_ID == catCombo.cycle_day_category_ID) {
                                        cycleDayAdapter.items.add(
                                            Pair(
                                                LAYOUT_CYCLE_DAY_EX,
                                                exCombo.cycle_day_exercise_ID
                                            )
                                        )
                                        cycleDayAdapter.idNamePairsExercise.add(
                                            Pair(
                                                exCombo.cycle_day_exercise_ID,
                                                exCombo.exercise_name
                                            )
                                        )
                                    }
                                }
                            }
                        }
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