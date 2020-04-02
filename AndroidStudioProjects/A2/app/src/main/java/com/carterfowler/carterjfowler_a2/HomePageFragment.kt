package com.carterfowler.carterjfowler_a2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomePageFragment : Fragment() {
    interface Callbacks {
        fun onSettingsSelectedinHome()
        fun onHistorySelecetedinHome()
        fun onNewGameSelectedinHome()
        fun onExitSelectedinHome()
    }

    private var callbacks: Callbacks? = null

    private val logTag = "448.HomePageFrag"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.home_page, menu)
        //Need to figure out how to turn off the up button
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.home_page, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag, "onActivityCreated() called")
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
        super.onPause()
        Log.d(logTag, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(logTag, "onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(logTag, "onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy() called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(logTag, "onDetach() called")
        callbacks = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(logTag, "onOptionsItemSelected() called")
        return when(item.itemId) {
            R.id.new_game -> {
                Toast.makeText(context, "New game will launch", Toast.LENGTH_SHORT).show()
                callbacks?.onNewGameSelectedinHome()
                true
            }
            R.id.exit -> {
                Toast.makeText(context, "Game will exit", Toast.LENGTH_SHORT).show()
                callbacks?.onExitSelectedinHome()
                true
            }
            R.id.history -> {
                Toast.makeText(context, "History page will launch", Toast.LENGTH_SHORT).show()
                callbacks?.onHistorySelecetedinHome()
                true
            }
            R.id.settings -> {
                callbacks?.onSettingsSelectedinHome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}