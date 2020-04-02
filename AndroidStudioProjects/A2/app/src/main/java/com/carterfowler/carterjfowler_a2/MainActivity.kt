package com.carterfowler.carterjfowler_a2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.carterfowler.carterjfowler_a2.history.GameListFragment

class MainActivity : AppCompatActivity(), HomePageFragment.Callbacks, GameListFragment.Callbacks {

    private val logTag = "448.MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null) {
            val fragment = HomePageFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")
    }

    override fun onPause() {
        Log.d(logTag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(logTag, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onHistorySelecetedinHome() {
        val fragment = GameListFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onExitSelectedinHome() {
        //TODO Figure out and add exit code, not sure what Paone meant for this to do
    }

    override fun onNewGameSelectedinHome() {
        //TODO Add code to launch new game
    }

    override fun onSettingsSelectedinHome() {
        val fragment = PreferencesFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }


    override fun onExitSelectedinHist() {
        //TODO Figure out and add exit code, not sure what Paone meant for this to do
    }

    override fun onNewGameSelectedinHist() {
        //TODO Add code to launch new game
    }

    override fun onSettingsSelectedinHist() {
        val fragment = PreferencesFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }
}
