package com.carterjfowler.carterjfowler_a3.History

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carterjfowler.carterjfowler_a3.R
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import com.carterjfowler.carterjfowler_a3.Data.LocationData

class HistoryListFragment : Fragment() {

    private val logTag = "448.HistListFrag"
    private lateinit var historyListViewModel: HistoryListViewModel
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var adapter: HistoryListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(logTag, "onCreate() called")

        val factory = HistoryListViewModelFactory(requireContext())
        historyListViewModel = ViewModelProvider(this, factory).get(HistoryListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.history_page, container, false)

        historyRecyclerView = view.findViewById(R.id.history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = HistoryListAdapter(historyListViewModel)
        historyRecyclerView.adapter = adapter

        val itemTouchHelperCallback = SwipeToDeleteHelper(adapter)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(historyRecyclerView)

//        updateUI(emptyList())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
        historyListViewModel.locationListLiveData.observe(
            viewLifecycleOwner,
            Observer { locations ->
                locations?.let {
                    Log.i(logTag, "Got location data ${locations.size}")
                    updateUI(locations)
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
    }

    private fun updateUI(locations: PagedList<LocationData>) {
        adapter.submitList(locations)
    }
}