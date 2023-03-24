package com.example.lightweight.ui.workouttracking.settracker.stopwatch


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.AndroidTestUtil
import com.example.lightweight.R
import com.example.lightweight.ui.workouttracking.settracker.stopwatch.StopwatchFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class StopwatchFragmentTest {
    @Test
    fun testStopwatchFragmentInView() {
        launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.constraint_layout_stopwatch)).check(matches(isDisplayed()))
    }

    @Test
    fun testClickStart_stopwatchStarts() {
        launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
        onView(withId(R.id.button_toggle)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(not(withText("00:00:00"))))

        // Reset the stopwatch
        onView(withId(R.id.button_toggle)).perform(click())
        onView(withId(R.id.image_view_reset)).perform(click())
    }

    @Test
    fun testClickPause_stopwatchPauses() {
        launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
        onView(withId(R.id.button_toggle)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.button_toggle)).perform(click())
        onView(withId(R.id.text_view_stopwatch_value)).check(matches(not(withText("00:00:00"))))
        val timePassed = AndroidTestUtil.getText(onView(withId(R.id.text_view_stopwatch_value)))

        Thread.sleep(1000)

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText(timePassed)))

        // Reset the stopwatch
        onView(withId(R.id.image_view_reset)).perform(click())
    }

    @Test
    fun testClickReset_stopwatchResets() {
        launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
        onView(withId(R.id.button_toggle)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.button_toggle)).perform(click())

        onView(withId(R.id.image_view_reset)).perform(click())

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
    }
}