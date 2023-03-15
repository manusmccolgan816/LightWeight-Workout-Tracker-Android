package com.example.lightweight.ui.activities

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.example.lightweight.R
import com.example.lightweight.ui.settracker.SetTrackerActivity
import org.junit.Test

class SetTrackerActivityTest {
    @Test
    fun testSetTrackerActivityInView() {
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SetTrackerActivity::class.java
        ).apply {
            putExtra("exerciseID", 1)
            putExtra("selectedDate", "2022-12-04")
        }
        val scenario = ActivityScenario.launch<SetTrackerActivity>(intent)

        onView(withId(R.id.relative_layout_set_tracker)).check(matches(isDisplayed()))
    }
}