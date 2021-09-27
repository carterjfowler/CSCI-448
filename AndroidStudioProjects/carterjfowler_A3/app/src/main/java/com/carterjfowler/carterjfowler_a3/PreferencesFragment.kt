package com.carterjfowler.carterjfowler_a3

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.carterjfowler.carterjfowler_a3.History.HistoryListViewModel
import com.carterjfowler.carterjfowler_a3.History.HistoryListViewModelFactory

class PreferencesFragment : PreferenceFragmentCompat() {

    private val logTag = "448.PreferenceFrag"
    private lateinit var historyListViewModel: HistoryListViewModel
    private var delete_button: Preference? = null
    private var save_locations_switch: SwitchPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        Log.d(logTag, "onCreatePreferences() called")

        val factory = HistoryListViewModelFactory(requireContext())
        historyListViewModel = ViewModelProvider(this, factory).get(HistoryListViewModel::class.java)

        delete_button = findPreference("clear_data")
        delete_button?.setOnPreferenceClickListener {
            val context = requireContext()
            AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete)
                .setMessage(context.resources.getString(R.string.confirm_delete_message))
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    historyListViewModel.deleteEntries()
                    Toast.makeText(context, "All history entries have been deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(android.R.string.no) {_, _ -> Toast.makeText(context, "You chose to not delete all the entries", Toast.LENGTH_SHORT).show()}
                .show()
            true
        }

        save_locations_switch = findPreference("save_location_switch")
    }


}