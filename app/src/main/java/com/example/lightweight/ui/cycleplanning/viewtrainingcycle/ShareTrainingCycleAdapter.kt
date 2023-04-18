package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.CycleDay

class ShareTrainingCycleAdapter(
    private var items: ArrayList<Pair<Int, Int?>>,
    private var cycleDays: ArrayList<CycleDay>,
    private var idNamePairsCategory: ArrayList<Pair<Int?, String>>,
    private var idNamePairsExercise: ArrayList<Pair<Int?, String>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val LAYOUT_CYCLE_DAY = 0
        const val LAYOUT_CYCLE_DAY_CAT = 1
        const val LAYOUT_CYCLE_DAY_EX = 2
    }

    var checkedCycleDays = arrayListOf<CycleDay>()

    private lateinit var checkBoxCycleDay: AppCompatCheckBox

    private lateinit var textViewTrainingCycleDayCategory: TextView

    private lateinit var textViewTrainingCycleDayExercise: TextView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_share_training_cycle_day, parent, false)
                ShareTrainingCycleDayViewHolder(view)
            }
            TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY_CAT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_share_training_cycle_day_category, parent, false)
                ShareTrainingCycleDayCategoryViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_share_training_cycle_day_exercise, parent, false)
                ShareTrainingCycleDayExerciseViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            LAYOUT_CYCLE_DAY -> {
                var numPriorCycleDays = 0
                for (i in 0..position) {
                    if (items[i].first != TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY) {
                        numPriorCycleDays++
                    }
                }

                val curCycleDay = cycleDays[position - numPriorCycleDays]

                checkBoxCycleDay = holder.itemView.findViewById(R.id.check_box_cycle_day)

                checkBoxCycleDay.text = curCycleDay.cycleDayName

                checkedCycleDays.add(curCycleDay)
                checkBoxCycleDay.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        checkedCycleDays.add(curCycleDay)
                    } else {
                        checkedCycleDays.remove(curCycleDay)
                    }
                }
            }
            LAYOUT_CYCLE_DAY_CAT -> {
                var numPriorCycleDayCats = 0
                for (i in 0..position) {
                    if (items[i].first != TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY_CAT) {
                        numPriorCycleDayCats++
                    }
                }

                val curCategoryName = idNamePairsCategory[position - numPriorCycleDayCats].second

                textViewTrainingCycleDayCategory =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_category)

                textViewTrainingCycleDayCategory.text = curCategoryName
            }
            LAYOUT_CYCLE_DAY_EX -> {
                var numPriorCycleDayExs = 0
                for (i in 0..position) {
                    if (items[i].first != TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY_EX) {
                        numPriorCycleDayExs++
                    }
                }

                val curExerciseName = idNamePairsExercise[position - numPriorCycleDayExs].second

                textViewTrainingCycleDayExercise =
                    holder.itemView.findViewById(R.id.text_view_training_cycle_day_exercise)

                textViewTrainingCycleDayExercise.text = curExerciseName
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].first) {
            TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY -> TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY
            TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY_CAT -> TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY_CAT
            else -> TrainingCycleDayAdapter.LAYOUT_CYCLE_DAY_EX
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ShareTrainingCycleDayViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class ShareTrainingCycleDayCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class ShareTrainingCycleDayExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}