package com.example.lightweight.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(R.layout.fragment_calendar), CalendarAdapter.OnItemListener {

    private lateinit var selectedDate: LocalDate

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

        selectedDate = LocalDate.now() // Select the current date by default
        setMonthView()

        buttonBack.setOnClickListener {
            // Navigate to the previous month
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        buttonForward.setOnClickListener {
            // Navigate to the next month
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
    }

    private fun setMonthView() {
        textViewMonthYear.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity?.applicationContext, 7)
        recyclerViewCalendar.layoutManager = layoutManager
        recyclerViewCalendar.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = arrayListOf<String>()
        val yearMonth: YearMonth = YearMonth.from(date)

        val daysInMonth: Int = yearMonth.lengthOfMonth()

        val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
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
                daysInMonthArray.add((i + 1 - dayOfWeek).toString())
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
            // ...display a toast giving the selected date
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}