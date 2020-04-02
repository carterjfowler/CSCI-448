package edu.mines.csci448.lab.criminalintent.ui.pager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import edu.mines.csci448.lab.criminalintent.R
import edu.mines.csci448.lab.criminalintent.data.Crime
import edu.mines.csci448.lab.criminalintent.ui.detail.CrimeDetailFragment
import java.util.*

private const val ARG_CRIME_ID = "crime_id"

class CrimePagerFragment: Fragment() {
    private val logTag = "448.CrimePagerFragment"
    private lateinit var crimeViewPager: ViewPager2
    private lateinit var crimePagerViewModel: CrimePagerViewModel
    private lateinit var adapter: CrimePagerAdapter
    private lateinit var currentCrime: UUID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        val factory = CrimePagerViewModelFactory(requireContext())
        crimePagerViewModel = ViewModelProvider(this, factory).get(CrimePagerViewModel::class.java)
        currentCrime = arguments?.getSerializable(edu.mines.csci448.lab.criminalintent.ui.pager.ARG_CRIME_ID) as UUID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_pager, container, false)
        crimeViewPager = view.findViewById(R.id.crime_view_pager)
        updateUI(emptyList())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
        crimePagerViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.d(logTag, "Got ${crimes.size} crimes")
                    updateUI(crimes)
                }
            }
        )
    }


    companion object {
        fun newInstance(crimeId: UUID): CrimePagerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimePagerFragment().apply { arguments = args }
        }
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

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimePagerAdapter(this, crimes)
        crimeViewPager.adapter = adapter
        crimes.forEachIndexed { position, crime ->
            if( crime.id == currentCrime ) {
                crimeViewPager.currentItem = position
                return
            }
        }
    }
}