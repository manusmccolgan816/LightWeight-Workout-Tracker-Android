package com.example.lightweight.ui.cycleplanning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.data.db.entities.Cycle

class TrainingCycleItemAdapter(
    var cycles: List<Cycle>,
    private val fragment: SelectTrainingCycleFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val logTag = "TrainingCycleItemAdapter"

    private lateinit var parent: ViewGroup

    private val layoutWithDesc = 0
    private val layoutNoDesc = 1

    private lateinit var textViewTrainingCycleName: TextView
    private lateinit var textViewTrainingCycleDesc: TextView
    private lateinit var imageViewTrainingCycleOptions: ImageView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        this.parent = parent

        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            layoutNoDesc -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle, parent, false)
                TrainingCycleItemViewHolderNoDesc(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_training_cycle_desc, parent, false)
                TrainingCycleItemViewHolderWithDesc(view)
            }
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        // If the Cycle has a description it will use a different xml layout file
        return if (cycles[position].description != null) {
            layoutWithDesc
        } else {
            layoutNoDesc
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curCycle = cycles[position]

        textViewTrainingCycleName = holder.itemView.findViewById(R.id.text_view_training_cycle_name)
        imageViewTrainingCycleOptions =
            holder.itemView.findViewById(R.id.image_view_training_cycle_options)

        // Display the training cycle name
        textViewTrainingCycleName.text = curCycle.cycleName

        if (holder.itemViewType == layoutWithDesc) {
            textViewTrainingCycleDesc =
                holder.itemView.findViewById(R.id.text_view_training_cycle_desc)

            // Display the training cycle description
            textViewTrainingCycleDesc.text = curCycle.description
        }

        imageViewTrainingCycleOptions.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return cycles.size
    }

    /**
     * This is the view holder that will use the layout for a Cycle with a description.
     */
    inner class TrainingCycleItemViewHolderWithDesc(view: View) : RecyclerView.ViewHolder(view)

    /**
     * This is the view holder that will use the layout for a Cycle without a description.
     */
    inner class TrainingCycleItemViewHolderNoDesc(view: View) : RecyclerView.ViewHolder(view)
}