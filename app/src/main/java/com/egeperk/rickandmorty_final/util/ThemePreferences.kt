package com.egeperk.rickandmorty_final.util

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.prefs.Preferences

class ThemePreferences(context: Context) {

    companion object {
        const val DARK_THEME = "dark.theme"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var darkMode = preferences.getInt(DARK_THEME,0)
    set(value) = preferences.edit().putInt(DARK_THEME,value).apply()
}