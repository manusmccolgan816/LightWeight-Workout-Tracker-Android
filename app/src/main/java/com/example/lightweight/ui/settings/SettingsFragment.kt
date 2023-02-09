package com.example.lightweight.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.lightweight.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Set the action bar title
        activity?.title = resources.getString(R.string.string_settings)
    }
}