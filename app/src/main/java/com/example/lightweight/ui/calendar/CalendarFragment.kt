package com.example.lightweight.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.MainActivity
import com.example.lightweight.ui.workout.WorkoutViewModel
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import com.example.lightweight.util.CalendarUtil.calculateMonthArray
import com.example.lightweight.util.CalendarUtil.getMonthYearFromDate
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate

class CalendarFragment : Fragment(R.layout.fragment_calendar), CalendarAdapter.OnItemListener,
    KodeinAware {

    private val logTag = "CalendarFragment"

    private val args: CalendarFragmentArgs by navArgs()

    override val kodein by kodein()
    private val workoutFactory: WorkoutViewModelFactory by instance()
    private val workoutViewModel: WorkoutViewModel by viewModels { workoutFactory }

    private lateinit var selectedDate: LocalDate
    private var selectedDatePosition: Int? = null
    private lateinit var displayDate: LocalDate
    private val today: LocalDate = LocalDate.now()
    private var todayPosition: Int? = null
    private var workoutPositions: ArrayList<Int> = ArrayList()
    private var workoutDates: List<LocalDate> = ArrayList()

    private lateinit var textViewMonthYear: TextView
    private lateinit var recyclerViewCalendar: RecyclerView
    private lateinit var imageViewPrevMonth: ImageView
    private lateinit var imageViewNextMonth: ImageView
    private lateinit var textViewNumWorkouts: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity!!::class == MainActivity::class) {
            // Set the toolbar title
            val textViewToolbarTitle = activity?.findViewById(R.id.text_view_toolbar_title) as TextView
            textViewToolbarTitle.text = resources.getString(R.string.string_select_date)

            // Remove the share icon from the toolbar
            val imageViewShareWorkout =
                activity?.findViewById(R.id.image_view_share_workout) as ImageView
            imageViewShareWorkout.visibility = View.GONE

            // Remove the select date icon from the toolbar
            val imageViewSelectDate = activity?.findViewById(R.id.image_view_select_date) as ImageView
            imageViewSelectDate.visibility = View.GONE
        }

        textViewMonthYear = view.findViewById(R.id.text_view_month_year)
        recyclerViewCalendar = view.findViewById(R.id.recycler_view_calendar)
        imageViewPrevMonth = view.findViewById(R.id.image_view_prev_month)
        imageViewNextMonth = view.findViewById(R.id.image_view_next_month)
        textViewNumWorkouts = view.findViewById(R.id.text_view_num_workouts)

        selectedDate = LocalDate.parse(args.selectedDate)
        displayDate = selectedDate // The selected date will be the date displayed by default

        workoutViewModel.getWorkoutDates().observe(viewLifecycleOwner) { workoutDateStrings ->
            // Convert each String date to LocalDate
            workoutDates = workoutDateStrings.map { LocalDate.parse(it) }

            // Display the total number of workouts completed
            val numWorkouts = workoutDateStrings.size
            if (numWorkouts == 1) {
                textViewNumWorkouts.text =
                    resources.getString(R.string.string_total_workout, numWorkouts)
            } else {
                textViewNumWorkouts.text =
                    resources.getString(R.string.string_total_workouts, numWorkouts)
            }

            setMonthView()
        }

        imageViewPrevMonth.setOnClickListener {
            // Display the previous month
            displayDate = displayDate.minusMonths(1)
            setMonthView()
        }

        imageViewNextMonth.setOnClickListener {
            // Display the next month
            displayDate = displayDate.plusMonths(1)
            setMonthView()
        }
    }

    /**
     * Displays the month dates.
     */
    private fun setMonthView() {
        workoutPositions = arrayListOf() // Ensure workoutPositions is repopulated from scratch
        textViewMonthYear.text = getMonthYearFromDate(displayDate)
        val grid = calculateMonthArray(
            displayDate,
            selectedDate,
            today,
            workoutPositions,
            workoutDates
        )
        val daysInMonth = grid.first
        selectedDatePosition = grid.second
        todayPosition = grid.third

        // If the selected date's month is the same as the month being displayed...
        val calendarAdapter: CalendarAdapter = if (displayDate.month.equals(selectedDate.month)) {
            // ...pass selectedDatePosition to highlight the selected date
            CalendarAdapter(
                daysInMonth,
                selectedDatePosition,
                todayPosition,
                workoutPositions,
                this,
                this
            )

        } else {
            // ...pass a null value so that no date is highlighted
            CalendarAdapter(daysInMonth, null, todayPosition, workoutPositions, this, this)
        }

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity?.applicationContext, 7)
        recyclerViewCalendar.layoutManager = layoutManager
        recyclerViewCalendar.adapter = calendarAdapter
    }

    override fun onItemClick(position: Int, dayText: String) {
        // If a non-empty date was selected...
        if (dayText != "") {
            // Get the new selected date in the format d MMMM yyyy
            val newSelectedDateStr = "$dayText ${getMonthYearFromDate(displayDate)}"

            // Navigate to SelectCategoryFragment
            val action = CalendarFragmentDirections
                .actionCalendarFragmentToHomeFragment(newSelectedDateStr)
            findNavController().navigate(action)
        }
    }
}