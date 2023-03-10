package com.example.lightweight.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object CalendarUtil {

    /**
     * Returns a Triple where the first value is an array containing the days in the month to
     * display on a 6 * 7 grid, where empty strings are empty grid spaces. The second and third
     * values are the selected date's position and today's position in the array respectively.
     */
    fun calculateMonthArray(
        displayDate: LocalDate,
        selectedDate: LocalDate,
        today: LocalDate,
        workoutPositions: ArrayList<Int>,
        workoutDates: List<LocalDate>
    ): Triple<ArrayList<String>, Int?, Int?> {
        val daysInMonthArray = arrayListOf<String>()
        val yearMonth: YearMonth = YearMonth.from(displayDate)

        val numDaysInMonth: Int = yearMonth.lengthOfMonth()

        val firstOfMonth: LocalDate = displayDate.withDayOfMonth(1)
        val dayOfWeek: Int = firstOfMonth.dayOfWeek.value

        var selectedDatePositionVar: Int? = null
        var todayPositionVar: Int? = null

        // Loop through each item in the 6 * 7 table
        for (i in 1..42) {
            // Ensure that the calendar squares before and after the first and last dates in the
            // month are left blank
            if (i < dayOfWeek || i >= numDaysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            }
            // Fill the correct squares with the day's date
            else {
                val monthDay: Int = i + 1 - dayOfWeek // Calculate the month day, i.e. 1st, 2nd...
                daysInMonthArray.add(monthDay.toString())

                // If this is the selected date...
                if (displayDate == selectedDate && selectedDate.dayOfMonth == monthDay) {
                    // Take note of its position
                    selectedDatePositionVar = i - 1
                }
                // If this is today's date...
                if (displayDate.month.equals(today.month)
                    && displayDate.year == today.year
                    && today.dayOfMonth == monthDay) {
                    // Take note of its position
                    todayPositionVar = i - 1
                }

                val thisDate = LocalDate.of(yearMonth.year, yearMonth.month, monthDay)
                // If there is a workout saved on this date...
                if (workoutDates.contains(thisDate)) {
                    // Take note of its position
                    workoutPositions.add(i - 1)
                }
            }
        }

        return Triple(daysInMonthArray, selectedDatePositionVar, todayPositionVar)
    }

    /**
     * Returns the month and year of the given date in the pattern MMMM yyyy.
     */
    fun getMonthYearFromDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }
}