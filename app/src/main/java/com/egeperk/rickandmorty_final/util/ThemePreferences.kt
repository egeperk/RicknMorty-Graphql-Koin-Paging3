package com.egeperk.rickandmorty_final.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.egeperk.rickandmorty_final.util.Constants.DARK_THEME

class ThemePreferences(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var darkMode = preferences.getInt(DARK_THEME,0)
    set(value) = preferences.edit().putInt(DARK_THEME,value).apply()
}