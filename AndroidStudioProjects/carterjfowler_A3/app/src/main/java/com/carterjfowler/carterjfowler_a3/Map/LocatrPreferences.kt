package com.carterjfowler.carterjfowler_a3.Map

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit


class LocatrPreferences(context: Context) {
    private val logTag = "448.LocatrPreferences"

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

}