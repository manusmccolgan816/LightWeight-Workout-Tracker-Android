package com.example.lightweight.ui.workouttracking.settracker.logsets

import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.lightweight.R
import com.example.lightweight.ui.workouttracking.settracker.stopwatch.StopwatchFragment
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class StopwatchFragmentTest {
    @Test
    fun testStopwatchFragmentInView() {
        val scenario = launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.constraint_layout_stopwatch)).check(matches(isDisplayed()))
    }

    @Test
    fun testClickStart_stopwatchStarts() {
        val scenario = launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
        onView(withId(R.id.button_toggle)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(not(withText("00:00:00"))))
    }

    @Test
    fun testClickPause_stopwatchPauses() {
        val scenario = launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
        onView(withId(R.id.button_toggle)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.button_toggle)).perform(click())
        onView(withId(R.id.text_view_stopwatch_value)).check(matches(not(withText("00:00:00"))))
        val timePassed = getText(onView(withId(R.id.text_view_stopwatch_value)))

        Thread.sleep(1000)

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText(timePassed)))
    }

    @Test
    fun testClickReset_stopwatchResets() {
        val scenario = launchFragmentInContainer<StopwatchFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
        onView(withId(R.id.button_toggle)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.button_toggle)).perform(click())

        onView(withId(R.id.image_view_reset)).perform(click())

        onView(withId(R.id.text_view_stopwatch_value)).check(matches(withText("00:00:00")))
    }

    fun getText(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }
}