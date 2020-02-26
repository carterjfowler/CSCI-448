package edu.mines.csci448.lab.criminalintent.ui.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.mines.csci448.lab.criminalintent.R
import edu.mines.csci448.lab.criminalintent.data.Crime
import java.util.*
import androidx.lifecycle.Observer


private const val ARG_CRIME_ID = "crime_id"

class CrimeDetailFragment : Fragment() {

    private val logTag = "448.CrimeDetailFrag"
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var crimeDetailViewModel: CrimeDetailViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        crime = Crime()
        val factory = CrimeDetailViewModelFactory(requireContext())
        crimeDetailViewModel = ViewModelProvider(this, factory)
            .get(CrimeDetailViewModel::class.java)
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        titleField = view.findViewById(R.id.crime_title_edit_text) as EditText
        dateButton = view.findViewById(R.id.crime_date_button) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved_checkbox) as CheckBox
        dateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag, "onActivityCreated() called")
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        val titleWatcher = object: TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener {
                _, isChecked -> crime.isSolved = isChecked
            }
        }
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeDetailFragment().apply { arguments = args }
        }
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
        crimeDetailViewModel.saveCrime(crime)
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

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }
}