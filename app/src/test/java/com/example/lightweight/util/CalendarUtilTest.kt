package com.example.lightweight.util

import com.example.lightweight.util.CalendarUtil.calculateMonthArray
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate

class CalendarUtilTest {

    // The following tests test the first value returned by calculateMonthArray

    @Test
    fun `calculateMonthArray display date March 2023, returns correct array`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val result = calculateMonthArray(
            displayDate,
            displayDate,
            displayDate,
            arrayListOf(),
            arrayListOf()
        )

        val expectedResult = arrayListOf<String>()
        for (i in 1..42) {
            if (i < 3 || i > 33) {
                expectedResult.add("")
            } else if (i in 3..33) {
                expectedResult.add((i - 2).toString())
            }
        }
        assertThat(result.first).isEqualTo(expectedResult)
    }

    // The following tests test the second value returned by calculateMonthArray

    @Test
    fun `selected date month != display date month, return null`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val selectedDate = LocalDate.parse("2024-08-01")
        val result = calculateMonthArray(
            displayDate,
            selectedDate,
            displayDate,
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.second).isNull()
    }

    @Test
    fun `selected date == display date, return correct position`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val selectedDate = LocalDate.parse("2023-03-03")
        val result = calculateMonthArray(
            displayDate,
            selectedDate,
            displayDate,
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.second).isEqualTo(4)
    }

    // The following tests test the third value returned by calculateMonthArray

    @Test
    fun `display date year == today year and display date month != today month, return null`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val today = LocalDate.parse("2023-04-03")
        val result = calculateMonthArray(
            displayDate,
            displayDate,
            today,
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.third).isNull()
    }

    @Test
    fun `display date year != today year and display date month == today month, return null`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val today = LocalDate.parse("2024-03-03")
        val result = calculateMonthArray(
            displayDate,
            displayDate,
            today,
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.third).isNull()
    }

    @Test
    fun `display date year != today year and display date month != today month, return null`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val today = LocalDate.parse("2024-04-03")
        val result = calculateMonthArray(
            displayDate,
            displayDate,
            today,
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.third).isNull()
    }

    @Test
    fun `display date month and year == today month and year, return correct position`() {
        val displayDate = LocalDate.parse("2023-03-03")
        val today = LocalDate.parse("2023-03-03")
        val result = calculateMonthArray(
            displayDate,
            displayDate,
            today,
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.third).isEqualTo(4)
    }
}