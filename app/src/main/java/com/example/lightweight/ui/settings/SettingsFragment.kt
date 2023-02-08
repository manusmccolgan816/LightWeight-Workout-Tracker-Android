package com.example.lightweight.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.lightweight.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var switchHighlightPersonalRecords: SwitchCompat
    private lateinit var switchKeepScreenOn: SwitchCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the action bar title
        activity?.title = resources.getString(R.string.string_settings)

        switchHighlightPersonalRecords = view.findViewById(R.id.switch_highlight_personal_records)
        switchKeepScreenOn = view.findViewById(R.id.switch_keep_screen_on)
    }
}