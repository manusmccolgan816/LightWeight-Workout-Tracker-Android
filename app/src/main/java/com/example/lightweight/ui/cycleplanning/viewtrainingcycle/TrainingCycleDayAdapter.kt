package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Category
import com.example.lightweight.data.db.entities.CycleDay
import com.example.lightweight.data.db.entities.CycleDayCategory
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TrainingCycleDayAdapter(
    var items: ArrayList<Pair<Int, Int?>>,
    var cycleDays: List<CycleDay>,
    val setCategory: (Category) -> Unit,
    var idNamePairs: ArrayList<Pair<Int?, String>>,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KodeinAware {

    companion object {
        const val LAYOUT_CYCLE_DAY = 0
        const val LAYOUT_CYCLE_DAY_CAT = 1
    }

    private val logTag = "TrainingCycleDayAdapter"

    override val kodein by kodein(fragment.requireContext())
    private val cycleDayCategoryFactory: CycleDayCategoryViewModelFactory by instance()
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by fragment.viewModels {
        cycleDayCategoryFactory
    }

    private lateinit var parent: ViewGroup

    private lateinit var textViewTrainingCycleDayName: TextView

    private lateinit var textViewTrainingCycleDayCategory: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent

        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            LAYOUT_CYCLE_DAY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_name, parent, false)
                TrainingCycleDayViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_category, parent, false)
                TrainingCycleDayCategoryViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            LAYOUT_CYCLE_DAY -> {
                Log.d(logTag, "onBindViewHolder layoutCycleDay")

                var numPriorValues = 0
                for (i in 0..position) {
                    if (items[i].first == LAYOUT_CYCLE_DAY_CAT) {
                        numPriorValues++
                    }
                }

                val curCycleDay = cycleDays[position - numPriorValues]

                textViewTrainingCycleDayName =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_name)!!

                textViewTrainingCycleDayName.text = curCycleDay.cycleDayName

                textViewTrainingCycleDayName.setOnClickListener {
                    val dialog = AddTrainingCycleDayCategoryDialogFragment(
                        fun(category: Category) {
                            val numCycleDaysObs =
                                cycleDayCategoryViewModel.getNumCycleDayCategoriesOfCycleDay(
                                    curCycleDay.cycleDayID
                                )
                            numCycleDaysObs.observe(fragment.viewLifecycleOwner) { numCycleDays ->
                                val cycleDayCategory =
                                    CycleDayCategory(
                                        curCycleDay.cycleDayID,
                                        category.categoryID,
                                        numCycleDays + 1
                                    )
                                cycleDayCategoryViewModel.insert(cycleDayCategory)
                                Log.d(logTag, "Inserting cycleDayCategory")
                                setCategory(category)

                                numCycleDaysObs.removeObservers(fragment.viewLifecycleOwner)
                            }
                        }
                    )
                    dialog.show(
                        fragment.requireActivity().supportFragmentManager,
                        "AddTrainingCycleDayCategory"
                    )
                }
            }
            LAYOUT_CYCLE_DAY_CAT -> {
                Log.d(logTag, "onBindViewHolder layoutCycleDayCategory")
                Log.d(logTag, items[position].first.toString())

                var numPriorValues = 0
                for (i in 0..position) {
                    if (items[i].first == LAYOUT_CYCLE_DAY) {
                        numPriorValues++
                    }
                }

                val curCategoryName = idNamePairs[position - numPriorValues].second
                val curCycleDayCategoryID = idNamePairs[position - numPriorValues].first

                textViewTrainingCycleDayCategory =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_category)

                textViewTrainingCycleDayCategory.text = curCategoryName
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].first == LAYOUT_CYCLE_DAY) {
            LAYOUT_CYCLE_DAY
        } else {
            LAYOUT_CYCLE_DAY_CAT
        }
    }

    override fun getItemCount(): Int {
        //return cycleDays?.size!!
        return items.size
    }

    inner class TrainingCycleDayViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class TrainingCycleDayCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}