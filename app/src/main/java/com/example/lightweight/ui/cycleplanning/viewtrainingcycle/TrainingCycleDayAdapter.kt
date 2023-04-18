package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.*
import com.example.lightweight.ui.category.CategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycleday.CycleDayViewModel
import com.example.lightweight.ui.cycleplanning.cycledaycategory.CycleDayCategoryViewModel
import com.example.lightweight.ui.cycleplanning.cycledayexercise.CycleDayExerciseViewModel
import com.example.lightweight.ui.exercise.ExerciseViewModel

class TrainingCycleDayAdapter(
    var items: ArrayList<Pair<Int, Int?>>,
    var cycleDays: ArrayList<CycleDay>,
    var idNamePairsCategory: ArrayList<Pair<Int?, String>>,
    var idNamePairsExercise: ArrayList<Pair<Int?, String>>,
    private val fragment: ViewTrainingCycleFragment,
    private val categoryViewModel: CategoryViewModel,
    private val exerciseViewModel: ExerciseViewModel,
    private val cycleDayViewModel: CycleDayViewModel,
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel,
    private val cycleDayExerciseViewModel: CycleDayExerciseViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val LAYOUT_CYCLE_DAY = 0
        const val LAYOUT_CYCLE_DAY_CAT = 1
        const val LAYOUT_CYCLE_DAY_EX = 2
    }

    private val logTag = "TrainingCycleDayAdapter"

    // Used to set the size of views in pixels
    private val scale: Float = fragment.requireContext().resources.displayMetrics.density

    private lateinit var parent: ViewGroup

    private lateinit var textViewTrainingCycleDayName: TextView
    private lateinit var imageViewAddCategory: ImageView
    private lateinit var imageViewDeleteDay: ImageView

    private lateinit var textViewTrainingCycleDayCategory: TextView
    private lateinit var imageViewAddExercise: ImageView
    private lateinit var imageViewDeleteCategory: ImageView

    private lateinit var textViewTrainingCycleDayExercise: TextView
    private lateinit var imageViewDeleteExercise: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent

        // Inflate the view with a different layout depending on the item type
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

                var numPriorCycleDays = 0
                for (i in 0..position) {
                    if (items[i].first != LAYOUT_CYCLE_DAY) {
                        numPriorCycleDays++
                    }
                }

                var curCycleDay = cycleDays[position - numPriorCycleDays]

                textViewTrainingCycleDayName =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_name)
                imageViewAddCategory = holder.itemView.findViewById(R.id.image_view_add_category)
                imageViewDeleteDay = holder.itemView.findViewById(R.id.image_view_delete_day)

                textViewTrainingCycleDayName.text = curCycleDay.cycleDayName
                textViewTrainingCycleDayName.setOnLongClickListener {
                    val popupMenu = PopupMenu(parent.context, holder.itemView, Gravity.START)
                    popupMenu.inflate(R.menu.cycle_day_popup_menu)
                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menu_item_edit_cycle_day -> {
                                EditTrainingCycleDayDialog(
                                    fragment.requireContext(),
                                    curCycleDay,
                                    fun(cycleDay: CycleDay) {
                                        cycleDayViewModel.update(cycleDay)
                                    }
                                ).show()
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                    true
                }

                imageViewAddCategory.setOnClickListener {
                    val dialog = AddTrainingCycleDayCategoryDialogFragment(
                        fun(category: Category) {
                            val numCycleDaysObs =
                                cycleDayCategoryViewModel.getNumCycleDayCategoriesOfCycleDay(
                                    curCycleDay.cycleDayID
                                )
                            numCycleDaysObs.observe(fragment.viewLifecycleOwner) { numCycleDays ->
                                numCycleDaysObs.removeObservers(fragment.viewLifecycleOwner)

                                val cycleDayCategory =
                                    CycleDayCategory(
                                        curCycleDay.cycleDayID,
                                        category.categoryID,
                                        numCycleDays + 1
                                    )
                                cycleDayCategoryViewModel.insert(cycleDayCategory)
                                Log.d(logTag, "Inserting cycleDayCategory")
                            }
                        },
                        categoryViewModel
                    )
                    dialog.show(
                        fragment.requireActivity().supportFragmentManager,
                        "AddTrainingCycleDayCategory"
                    )
                }

                imageViewDeleteDay.setOnClickListener {
                    ConfirmDeleteTrainingCycleDayDialog(
                        parent.context,
                        curCycleDay,
                        fun(cycleDay: CycleDay) {
                            val curCycleDayObs =
                                cycleDayViewModel.getCycleDayOfID(cycleDay.cycleDayID)
                            curCycleDayObs.observe(fragment.viewLifecycleOwner) {
                                curCycleDayObs.removeObservers(fragment.viewLifecycleOwner)

                                curCycleDay = it

                                cycleDayViewModel.delete(curCycleDay)
                                cycleDayViewModel.decrementCycleDayNumbersAfter(
                                    curCycleDay.cycleID,
                                    curCycleDay.cycleDayNumber
                                )
                            }
                        }
                    ).show()
                }
            }
            LAYOUT_CYCLE_DAY_CAT -> {
                Log.d(logTag, "onBindViewHolder layoutCycleDayCategory")

                holder.itemView.visibility = View.VISIBLE
                val params: ViewGroup.LayoutParams? = holder.itemView.layoutParams
                val heightPixels = (48 * scale + 0.5f).toInt()
                params?.height = heightPixels
                params?.width = ViewGroup.LayoutParams.MATCH_PARENT
                holder.itemView.layoutParams = params

                var numPriorCycleDayCats = 0
                for (i in 0..position) {
                    if (items[i].first != LAYOUT_CYCLE_DAY_CAT) {
                        numPriorCycleDayCats++
                    }
                }

                val curCategoryName = idNamePairsCategory[position - numPriorCycleDayCats].second
                val curCycleDayCategoryID =
                    idNamePairsCategory[position - numPriorCycleDayCats].first

                textViewTrainingCycleDayCategory =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_category)
                imageViewAddExercise = holder.itemView.findViewById(R.id.image_view_add_exercise)
                imageViewDeleteCategory =
                    holder.itemView.findViewById(R.id.image_view_delete_category)

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
                                    val cycleDayExercise = CycleDayExercise(
                                        curCycleDayCategoryID,
                                        exercise.exerciseID,
                                        numExercises + 1
                                    )

                                    cycleDayExerciseViewModel.insert(cycleDayExercise)
                                    Log.d(logTag, "Inserting cycleDayExercise")

                                    numExerciseObs.removeObservers(fragment.viewLifecycleOwner)
                                }
                            },
                            categoryViewModel,
                            exerciseViewModel
                        )
                        dialog.show(
                            fragment.requireActivity().supportFragmentManager,
                            "AddTrainingCycleDayExercise"
                        )
                        categoryIDObs.removeObservers(fragment.viewLifecycleOwner)
                    }
                }

                imageViewDeleteCategory.setOnClickListener {
                    ConfirmDeleteTrainingCycleDayCategoryDialog(
                        parent.context,
                        curCycleDayCategoryID,
                        fun(curCycleDayCategoryID: Int?) {
                            val cycleDayCategoryObs =
                                cycleDayCategoryViewModel.getCycleDayCategoryOfID(
                                    curCycleDayCategoryID
                                )
                            cycleDayCategoryObs.observe(fragment.viewLifecycleOwner) { cycleDayCategory ->
                                cycleDayCategoryObs.removeObservers(fragment.viewLifecycleOwner)

                                cycleDayCategoryViewModel.delete(cycleDayCategory)
                                cycleDayCategoryViewModel.decrementCycleDayCategoryNumbersAfter(
                                    cycleDayCategory.cycleDayID,
                                    cycleDayCategory.cycleDayCategoryNumber
                                )
                            }
                        }
                    ).show()
                }
            }
            LAYOUT_CYCLE_DAY_EX -> {
                Log.d(logTag, "onBindViewHolder layoutCycleDayExercise")

                holder.itemView.visibility = View.VISIBLE
                val params: ViewGroup.LayoutParams? = holder.itemView.layoutParams
                val heightPixels = (48 * scale + 0.5f).toInt()
                params?.height = heightPixels
                params?.width = ViewGroup.LayoutParams.MATCH_PARENT
                holder.itemView.layoutParams = params

                var numPriorCycleDayExs = 0
                for (i in 0..position) {
                    if (items[i].first != LAYOUT_CYCLE_DAY_EX) {
                        numPriorCycleDayExs++
                    }
                }

                val curExerciseName = idNamePairsExercise[position - numPriorCycleDayExs].second
                val curCycleDayExerciseID =
                    idNamePairsExercise[position - numPriorCycleDayExs].first

                textViewTrainingCycleDayExercise =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_exercise)
                imageViewDeleteExercise =
                    holder.itemView.findViewById(R.id.image_view_delete_exercise)

                textViewTrainingCycleDayExercise.text = curExerciseName

                imageViewDeleteExercise.setOnClickListener {
                    ConfirmDeleteTrainingCycleDayExerciseDialog(
                        parent.context,
                        curCycleDayExerciseID,
                        fun(curCycleDayExerciseID: Int?) {
                            val cycleDayExerciseObs =
                                cycleDayExerciseViewModel.getCycleDayExerciseOfID(
                                    curCycleDayExerciseID
                                )
                            cycleDayExerciseObs.observe(fragment.viewLifecycleOwner) { cycleDayExercise ->
                                cycleDayExerciseObs.removeObservers(fragment.viewLifecycleOwner)

                                cycleDayExerciseViewModel.delete(cycleDayExercise)
                                cycleDayExerciseViewModel.decrementCycleDayExerciseNumbersAfter(
                                    cycleDayExercise.cycleDayCategoryID,
                                    cycleDayExercise.cycleDayExerciseNumber
                                )
                            }
                        }
                    ).show()
                }
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