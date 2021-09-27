package com.carterfowler.carterjfowler_a2

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.carterfowler.carterjfowler_a2.history.GameListViewModel
import com.carterfowler.carterjfowler_a2.history.GameListViewModelFactory


class PreferencesFragment : PreferenceFragmentCompat() {

    private val logTag = "448.PreferenceFrag"
    private lateinit var gameListViewModel: GameListViewModel
    private var delete_button: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        Log.d(logTag, "onCreatePreferences() called")

        val factory = GameListViewModelFactory(requireContext())
        gameListViewModel = ViewModelProvider(this, factory).get(GameListViewModel::class.java)

        delete_button = findPreference("clear_data")
        delete_button?.setOnPreferenceClickListener {
            gameListViewModel.deleteEntries()
            Toast.makeText(context, "All history entries have been deleted", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.settings_page, menu)
    }


}