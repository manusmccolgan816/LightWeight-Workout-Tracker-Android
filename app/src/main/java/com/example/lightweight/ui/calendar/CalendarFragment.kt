package com.example.lightweight.ui.calendar

import android.os.Bundle
import android.util.Log
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
import com.example.lightweight.ui.workout.WorkoutViewModel
import com.example.lightweight.ui.workout.WorkoutViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the action bar title
        activity?.title = resources.getString(R.string.string_select_date)

        textViewMonthYear = view.findViewById(R.id.text_view_month_year)
        recyclerViewCalendar = view.findViewById(R.id.recycler_view_calendar)
        imageViewPrevMonth = view.findViewById(R.id.image_view_prev_month)
        imageViewNextMonth = view.findViewById(R.id.image_view_next_month)

        selectedDate = LocalDate.parse(args.selectedDate)
        displayDate = selectedDate // The selected date will be the date displayed by default

        workoutViewModel.getWorkoutDates().observe(viewLifecycleOwner) { workoutDateStrings ->
            // Convert each String date to LocalDate
            workoutDates = workoutDateStrings.map { LocalDate.parse(it) }

            setMonthView()
            for (i in workoutDates) {
                Log.d(logTag, "Workout dates: $i")
            }
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
        textViewMonthYear.text = monthYearFromDate(displayDate)
        val daysInMonth = daysInMonthArray(displayDate)

        // today's position is set to null if the displayed month is not the current month
        if (!(displayDate.month.equals(today.month))) {
            todayPosition = null
        }

        // If the selected date's month is the same as the month being displayed...
        val calendarAdapter: CalendarAdapter = if (displayDate.month.equals(selectedDate.month)) {
            // ...pass selectedDatePosition to highlight the selected date
            CalendarAdapter(
                daysInMonth,
                selectedDatePosition,
                todayPosition,
                workoutPositions,
                this
            )

        } else {
            // ...pass a null value so that no date is highlighted
            CalendarAdapter(daysInMonth, null, todayPosition, workoutPositions, this)
        }

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity?.applicationContext, 7)
        recyclerViewCalendar.layoutManager = layoutManager
        recyclerViewCalendar.adapter = calendarAdapter
    }

    /**
     * Returns an array containing the days in the month to display on a 6 * 7 grid, where empty
     * strings are empty grid spaces.
     */
    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = arrayListOf<String>()
        val yearMonth: YearMonth = YearMonth.from(date)

        val daysInMonth: Int = yearMonth.lengthOfMonth()

        val firstOfMonth: LocalDate = displayDate.withDayOfMonth(1)
        val dayOfWeek: Int = firstOfMonth.dayOfWeek.value

        // Loop through each item in the 6 * 7 table
        for (i in 1..42) {
            // Ensure that the calendar squares before and after the first and last dates in the
            // month are left blank
            if (i < dayOfWeek || i >= daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            }
            // Fill the correct squares with the day's date
            else {
                val monthDay: Int = i + 1 - dayOfWeek
                daysInMonthArray.add(monthDay.toString())

                if (displayDate.equals(selectedDate) && selectedDate.dayOfMonth == monthDay) {
                    selectedDatePosition = i - 1
                    Log.d(logTag, "Selected date position is $selectedDatePosition")
                }
                if (today.dayOfMonth == monthDay) {
                    todayPosition = i - 1
                    Log.d(logTag, "Today position is $todayPosition")
                }

                val thisDate = LocalDate.of(yearMonth.year, yearMonth.month, monthDay)
                if (workoutDates.contains(thisDate)) {
                    Log.d(logTag, "Found workout on date $thisDate")
                    workoutPositions.add(i - 1)
                }
            }
        }

        return daysInMonthArray
    }

    /**
     * Return the month and year of the given date in the pattern MMMM yyyy.
     */
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    override fun onItemClick(position: Int, dayText: String) {
        // If a non-empty date was selected...
        if (dayText != "") {
            // Get the new selected date in the format d MMMM yyyy
            val newSelectedDateStr = "$dayText ${monthYearFromDate(displayDate)}"

            // Navigate to SelectCategoryFragment
            val action = CalendarFragmentDirections
                .actionCalendarFragmentToHomeFragment(newSelectedDateStr)
            findNavController().navigate(action)
        }
    }
}