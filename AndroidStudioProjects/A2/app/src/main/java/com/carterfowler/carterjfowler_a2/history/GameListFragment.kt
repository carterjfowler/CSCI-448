package com.carterfowler.carterjfowler_a2.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carterfowler.carterjfowler_a2.R
import com.carterfowler.carterjfowler_a2.data.Game

class GameListFragment : Fragment() {
    interface Callbacks {
        fun onNewGameSelectedinHist()
        fun onSettingsSelectedinHist()
        fun onExitSelectedinHist()
    }

    private var callbacks: Callbacks? = null

    private val logTag = "448.HistListFrag"
    private lateinit var gameListViewModel: GameListViewModel
    private lateinit var gameRecyclerView: RecyclerView
    private lateinit var adapter: GameListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(logTag, "onCreate() called")

        val factory = GameListViewModelFactory(requireContext())
        gameListViewModel = ViewModelProvider(this, factory).get(GameListViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.history_page, menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.history_list, container, false)

        gameRecyclerView = view.findViewById(R.id.game_list_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI(emptyList())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
        gameListViewModel.gameListLiveData.observe(
            viewLifecycleOwner,
            Observer { games ->
                games?.let {
                    Log.i(logTag, "Got games ${games.size}")
                    updateUI(games)
                }
            }
        )
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

    private fun updateUI(games: List<Game>) {
//        val crimes = crimeListViewModel.crimeListLiveData
        adapter = GameListAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(logTag, "onOptionsItemSelected() called")
        return when(item.itemId) {
            R.id.new_game -> {
                Toast.makeText(context, "New game will launch", Toast.LENGTH_SHORT).show()
                callbacks?.onNewGameSelectedinHist()
                true
            }
            R.id.exit -> {
                Toast.makeText(context, "Game will exit", Toast.LENGTH_SHORT).show()
                callbacks?.onExitSelectedinHist()
                true
            }
            R.id.settings -> {
                callbacks?.onSettingsSelectedinHist()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}