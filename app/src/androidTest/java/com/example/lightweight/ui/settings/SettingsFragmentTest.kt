package com.example.lightweight.ui.settings

import com.example.lightweight.R
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsFragmentTest {
    @Test
    fun testSettingsFragmentInView() {
        launchFragmentInContainer<SettingsFragment>(
            themeResId = R.style.Theme_Lightweight
        )

        // Test that "Theme" is displayed
        onView(withText(R.string.string_theme)).check(matches(isDisplayed()))
    }
}