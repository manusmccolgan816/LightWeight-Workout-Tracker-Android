package com.example.lightweight.ui.cycleplanning.viewtrainingcycle

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
    var cycleDays: List<CycleDay>,
    val setCategory: (Category) -> Unit,
    private val fragment: Fragment
) : RecyclerView.Adapter<TrainingCycleDayAdapter.TrainingCycleDayViewHolder>(), KodeinAware {

    override val kodein by kodein(fragment.requireContext())
    private val cycleDayCategoryFactory: CycleDayCategoryViewModelFactory by instance()
    private val cycleDayCategoryViewModel: CycleDayCategoryViewModel by fragment.viewModels {
        cycleDayCategoryFactory
    }

    private lateinit var parent: ViewGroup

    private lateinit var textViewTrainingCycleDayName: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingCycleDayViewHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_cycle_day_name, parent, false)
        return TrainingCycleDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingCycleDayViewHolder, position: Int) {
        val curCycleDay = cycleDays[position]

        textViewTrainingCycleDayName =
            holder.itemView.findViewById(R.id.text_view_training_cycle_day_name)!!

        textViewTrainingCycleDayName.text = curCycleDay.cycleDayName

        textViewTrainingCycleDayName.setOnClickListener {
            val dialog = AddTrainingCycleDayCategoryDialogFragment(
                fun(category: Category) {
                    // TODO Sort out cycleDayCategoryNumber
                    val cycleDayCategory =
                        CycleDayCategory(curCycleDay.cycleDayID, category.categoryID, 0)
                    cycleDayCategoryViewModel.insert(cycleDayCategory)
                    setCategory(category)
                }
            )
            dialog.show(
                fragment.requireActivity().supportFragmentManager,
                "AddTrainingCycleDayCategory"
            )
        }
    }

    override fun getItemCount(): Int {
        return cycleDays.size
    }

    inner class TrainingCycleDayViewHolder(view: View) : RecyclerView.ViewHolder(view)
}