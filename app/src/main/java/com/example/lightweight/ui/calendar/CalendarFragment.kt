package com.example.lightweight.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.HomeFragmentDirections
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(R.layout.fragment_calendar), CalendarAdapter.OnItemListener {

    private val args: CalendarFragmentArgs by navArgs()

    private lateinit var selectedDate: LocalDate
    private var selectedDatePosition: Int? = null
    private lateinit var displayDate: LocalDate

    private lateinit var textViewMonthYear: TextView
    private lateinit var recyclerViewCalendar: RecyclerView
    private lateinit var buttonBack: Button
    private lateinit var buttonForward: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewMonthYear = view.findViewById(R.id.text_view_month_year)
        recyclerViewCalendar = view.findViewById(R.id.recycler_view_calendar)
        buttonBack = view.findViewById(R.id.button_back)
        buttonForward = view.findViewById(R.id.button_forward)

        selectedDate = LocalDate.parse(args.selectedDate)
        displayDate = selectedDate // The selected date will be the date displayed by default
        setMonthView()

        buttonBack.setOnClickListener {
            // Display the previous month
            displayDate = displayDate.minusMonths(1)
            setMonthView()
        }

        buttonForward.setOnClickListener {
            // Display the next month
            displayDate = displayDate.plusMonths(1)
            setMonthView()
        }
    }

    /**
     * Displays the month dates.
     */
    private fun setMonthView() {
        textViewMonthYear.text = monthYearFromDate(displayDate)
        val daysInMonth = daysInMonthArray(displayDate)

        // If the selected date's month is the same as the month being displayed...
        val calendarAdapter: CalendarAdapter = if (displayDate.month.equals(selectedDate.month)) {
            // ...pass selectedDatePosition to highlight the selected date
            CalendarAdapter(daysInMonth, selectedDatePosition, this)
        } else {
            // ...pass a null value so that no date is highlighted
            CalendarAdapter(daysInMonth, null, this)
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
            // Get the new selected date in the format dd MMMM yyyy
            val newSelectedDateStr = "$dayText ${monthYearFromDate(displayDate)}"

            // Navigate to SelectCategoryFragment
            val action = CalendarFragmentDirections
                .actionCalendarFragmentToHomeFragment(newSelectedDateStr)
            findNavController().navigate(action)
        }
    }
}