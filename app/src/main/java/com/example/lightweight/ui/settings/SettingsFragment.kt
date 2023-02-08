package com.example.lightweight.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.lightweight.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

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

    private suspend fun savePreference(key: String, value: String) {
        val settingsKey = stringPreferencesKey(key)
        requireContext().dataStore.edit { settings ->
            settings[settingsKey] = value
        }
    }

    private suspend fun readPreference(key: String): String? {
        val settingsKey = stringPreferencesKey(key)
        val preferences = requireContext().dataStore.data.first()

        return preferences[settingsKey]
    }
}