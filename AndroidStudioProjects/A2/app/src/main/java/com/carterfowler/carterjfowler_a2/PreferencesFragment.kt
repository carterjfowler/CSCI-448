package com.carterfowler.carterjfowler_a2

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.preference.PreferenceFragmentCompat

class PreferencesFragment : PreferenceFragmentCompat() {

    private val logTag = "448.PreferenceFrag"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        Log.d(logTag, "onCreatePreferences() called")
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.settings_page, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when( item.getItemId() ) {
            android.R.id.home -> {
                //Need to figure out what I have to do for the up button
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}