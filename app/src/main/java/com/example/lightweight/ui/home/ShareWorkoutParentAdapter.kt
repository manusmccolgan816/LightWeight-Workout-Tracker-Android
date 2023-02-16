package com.example.lightweight.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.IdNamePair
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.ExerciseInstance
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModel
import com.example.lightweight.ui.exerciseinstance.ExerciseInstanceViewModelFactory
import com.example.lightweight.ui.trainingset.TrainingSetViewModel
import com.example.lightweight.ui.trainingset.TrainingSetViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ShareWorkoutParentAdapter(
    var idNamePairs: List<IdNamePair>, // A list of exercise instance IDs and their exercise name
    private val fragment: Fragment
) : RecyclerView.Adapter<ShareWorkoutParentAdapter.ShareWorkoutParentViewHolder>(), KodeinAware {

    override val kodein by kodein(fragment.requireContext())
    private val exerciseInstanceFactory: ExerciseInstanceViewModelFactory by instance()
    private val exerciseInstanceViewModel: ExerciseInstanceViewModel by fragment.viewModels {
        exerciseInstanceFactory
    }
    private val trainingSetFactory: TrainingSetViewModelFactory by instance()
    private val trainingSetViewModel: TrainingSetViewModel by fragment.viewModels {
        trainingSetFactory
    }

    val checkedIDNamePairs: ArrayList<IdNamePair> = arrayListOf()

    private lateinit var checkBoxExerciseInstance: AppCompatCheckBox
    private lateinit var recyclerViewShareTrainingSets: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareWorkoutParentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_share_workout, parent, false)

        return ShareWorkoutParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareWorkoutParentViewHolder, position: Int) {
        val curID = idNamePairs[position].id
        val curName = idNamePairs[position].name

        checkBoxExerciseInstance = holder.itemView.findViewById(R.id.check_box_exercise_instance)
        recyclerViewShareTrainingSets =
            holder.itemView.findViewById(R.id.recycler_view_share_training_sets)

        checkBoxExerciseInstance.text = curName
        checkedIDNamePairs.add(idNamePairs[position])
        checkBoxExerciseInstance.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkedIDNamePairs.add(idNamePairs[position])
            } else {
                checkedIDNamePairs.remove(idNamePairs[position])
            }
        }

        val shareWorkoutChildAdapter = ShareWorkoutChildAdapter(
            listOf(),
            curID,
            fragment
        )
        recyclerViewShareTrainingSets.layoutManager = LinearLayoutManager(holder.itemView.context)
        recyclerViewShareTrainingSets.adapter = shareWorkoutChildAdapter

        val trainingSetsObs = trainingSetViewModel.getTrainingSetsOfExerciseInstance(curID)
        trainingSetsObs.observe(fragment.viewLifecycleOwner) {
            shareWorkoutChildAdapter.trainingSets = it
            shareWorkoutChildAdapter.notifyItemRangeChanged(0, it.size)

            trainingSetsObs.removeObservers(fragment.viewLifecycleOwner)
        }
    }

    override fun getItemCount(): Int {
        return idNamePairs.size
    }

    inner class ShareWorkoutParentViewHolder(view: View) : RecyclerView.ViewHolder(view)
}