package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.*
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModelFactory
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TrainingCycleDayAdapter(
    var items: ArrayList<Pair<Int, Int?>>,
    var cycleDays: ArrayList<CycleDay>,
    var idNamePairsCategory: ArrayList<Pair<Int?, String>>,
    var idNamePairsExercise: ArrayList<Pair<Int?, String>>,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KodeinAware {

    companion object {
        const val LAYOUT_CYCLE_DAY = 0
        const val LAYOUT_CYCLE_DAY_CAT = 1
        const val LAYOUT_CYCLE_DAY_EX = 2
    }

    private val logTag = "TrainingCycleDayAdapter"

    override val kodein by kodein(fragment.requireContext())
    private val cycleDayCategoryFactory: CycleDayCategoryViewModelFactory by instance()
    private val cycleDayExerciseFactory: CycleDayExerciseViewModelFactory by instance()

    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by fragment.viewModels {
        cycleDayCategoryFactory
    }
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel by fragment.viewModels {
        cycleDayExerciseFactory
    }

    private lateinit var parent: ViewGroup

    private lateinit var textViewTrainingCycleDayName: TextView
    private lateinit var imageViewAddCategory: ImageView

    private lateinit var textViewTrainingCycleDayCategory: TextView
    private lateinit var imageViewAddExercise: ImageView

    private lateinit var textViewTrainingCycleDayExercise: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent

        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            LAYOUT_CYCLE_DAY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_name, parent, false)
                TrainingCycleDayViewHolder(view)
            }
            LAYOUT_CYCLE_DAY_CAT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_category, parent, false)
                TrainingCycleDayCategoryViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_day_exercise, parent, false)
                TrainingCycleDayExerciseViewHolder(view)
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
                    if (items[i].first != LAYOUT_CYCLE_DAY) {
                        numPriorValues++
                    }
                }

                val curCycleDay = cycleDays[position - numPriorValues]

                textViewTrainingCycleDayName =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_name)
                imageViewAddCategory = holder.itemView.findViewById(R.id.image_view_add_category)

                textViewTrainingCycleDayName.text = curCycleDay.cycleDayName

                imageViewAddCategory.setOnClickListener {
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

                var numPriorValues = 0
                for (i in 0..position) {
                    if (items[i].first != LAYOUT_CYCLE_DAY_CAT) {
                        numPriorValues++
                    }
                }

                val curCategoryName = idNamePairsCategory[position - numPriorValues].second
                val curCycleDayCategoryID = idNamePairsCategory[position - numPriorValues].first

                textViewTrainingCycleDayCategory =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_category)
                imageViewAddExercise = holder.itemView.findViewById(R.id.image_view_add_exercise)

                textViewTrainingCycleDayCategory.text = curCategoryName

                imageViewAddExercise.setOnClickListener {
                    val categoryIDObs = cycleDayCategoryViewModel.getCategoryIDOfCycleDayCategoryID(
                        curCycleDayCategoryID
                    )
                    // Observe to get the category ID
                    categoryIDObs.observe(fragment.viewLifecycleOwner) { categoryID ->
                        val dialog = AddTrainingCycleDayExerciseDialogFragment(
                            categoryID,
                            fun(exercise: Exercise) {
                                val numExerciseObs =
                                    cycleDayExerciseViewModel.getNumCycleDayExercisesOfCycleDayCategory(
                                        curCycleDayCategoryID
                                    )
                                // Observe to get the number of CycleDayExercises in the CycleDayCategory
                                numExerciseObs.observe(fragment.viewLifecycleOwner) { numExercises ->
                                    val cycleDayIDObs =
                                        cycleDayCategoryViewModel.getCycleDayIDOfCycleDayCategoryID(
                                            curCycleDayCategoryID
                                        )
                                    // Observe to get the cycleDayID
                                    cycleDayIDObs.observe(fragment.viewLifecycleOwner) { cycleDayID ->
                                        val cycleDayExercise = CycleDayExercise(
                                            cycleDayID,
                                            curCycleDayCategoryID,
                                            exercise.exerciseID,
                                            numExercises + 1
                                        )

                                        cycleDayExerciseViewModel.insert(cycleDayExercise)
                                        Log.d(logTag, "Inserting cycleDayExercise")

                                        cycleDayIDObs.removeObservers(fragment.viewLifecycleOwner)
                                    }

                                    numExerciseObs.removeObservers(fragment.viewLifecycleOwner)
                                }
                            }
                        )
                        dialog.show(
                            fragment.requireActivity().supportFragmentManager,
                            "AddTrainingCycleDayExercise"
                        )
                        categoryIDObs.removeObservers(fragment.viewLifecycleOwner)
                    }
                }
            }
            LAYOUT_CYCLE_DAY_EX -> {
                Log.d(logTag, "onBindViewHolder layoutCycleDayExercise")

                var numPriorValues = 0
                for (i in 0..position) {
                    if (items[i].first != LAYOUT_CYCLE_DAY_EX) {
                        numPriorValues++
                    }
                }

                val curExerciseName = idNamePairsExercise[position - numPriorValues].second

                textViewTrainingCycleDayExercise =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_exercise)

                textViewTrainingCycleDayExercise.text = curExerciseName
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].first) {
            LAYOUT_CYCLE_DAY -> LAYOUT_CYCLE_DAY
            LAYOUT_CYCLE_DAY_CAT -> LAYOUT_CYCLE_DAY_CAT
            else -> LAYOUT_CYCLE_DAY_EX
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class TrainingCycleDayViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class TrainingCycleDayCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class TrainingCycleDayExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}