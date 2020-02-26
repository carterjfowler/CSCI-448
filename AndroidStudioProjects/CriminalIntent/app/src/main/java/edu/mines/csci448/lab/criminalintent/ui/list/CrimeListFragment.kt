package edu.mines.csci448.lab.criminalintent.ui.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.lab.criminalintent.R
import edu.mines.csci448.lab.criminalintent.data.Crime
import java.util.*

class CrimeListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    private val logTag = "448.CrimeListFrag"
    private lateinit var crimeListViewModel: CrimeListViewModel
    private lateinit var crimeRecyclerView: RecyclerView
    private lateinit var adapter: CrimeListAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")

        val factory = CrimeListViewModelFactory(requireContext())
        crimeListViewModel = ViewModelProvider(this, factory).get(CrimeListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_list_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI(emptyList())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(logTag, "Got crimes ${crimes.size}")
                    updateUI(crimes)
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

    private fun updateUI(crimes: List<Crime>) {
//        val crimes = crimeListViewModel.crimeListLiveData
        adapter = CrimeListAdapter(crimes) { crime: Crime -> Unit
            callbacks?.onCrimeSelected(crime.id)
        }
        crimeRecyclerView.adapter = adapter
    }

}