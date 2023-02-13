package com.example.lightweight.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.lightweight.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var prefsChanged = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the toolbar title
        val textViewToolbarTitle =
            requireActivity().findViewById<TextView>(R.id.text_view_toolbar_title)
        if (textViewToolbarTitle != null) {
            textViewToolbarTitle.text = resources.getString(R.string.string_settings)
        }

        // Remove the share icon
        val imageViewShareWorkout =
            requireActivity().findViewById<ImageView>(R.id.image_view_share_workout)
        if (imageViewShareWorkout != null) {
            imageViewShareWorkout.visibility = View.GONE
        }

        // Remove the select date icon
        val imageViewSelectDate =
            requireActivity().findViewById<ImageView>(R.id.image_view_select_date)
        if (imageViewSelectDate != null) {
            imageViewSelectDate.visibility = View.GONE
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null) {
            prefsChanged = true

            if (sharedPreferences.getBoolean("keepScreenOn", false)) {
                requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }

            if (sharedPreferences.getString(
                    "theme",
                    "System default"
                ) == "System default"
            ) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else if (sharedPreferences.getString(
                    "theme",
                    "System default"
                ) == "Light"
            ) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                requireActivity().recreate()
            } else if (sharedPreferences.getString(
                    "theme",
                    "System default"
                ) == "Dark"
            ) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Set up a listener when a key changes
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        // Unregister the listener whenever the key changes
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}