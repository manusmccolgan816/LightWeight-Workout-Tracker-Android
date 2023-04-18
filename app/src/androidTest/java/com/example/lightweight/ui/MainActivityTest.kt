package com.example.lightweight.ui

import androidx.test.core.app.ActivityScenario
import com.example.lightweight.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Test
    fun testMainActivityInView() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }
}