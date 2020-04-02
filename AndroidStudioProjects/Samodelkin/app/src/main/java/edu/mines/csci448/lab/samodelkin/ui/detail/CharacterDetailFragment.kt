package edu.mines.csci448.lab.samodelkin.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.mines.csci448.lab.samodelkin.R
import edu.mines.csci448.lab.samodelkin.data.Character
import edu.mines.csci448.lab.samodelkin.ui.list.CharacterListFragmentDirections
import java.util.*

private const val ARGS_CHARCTER_ID = "characterId"

class CharacterDetailFragment : Fragment() {

    companion object {
        fun newInstance(characterId: UUID): CharacterDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARGS_CHARCTER_ID, characterId)
            }
            return CharacterDetailFragment().apply {
                arguments = args
            }
        }
    }

    private val logTag = "448.CharDetailFrag"

    private val args: CharacterDetailFragmentArgs by navArgs()

    private lateinit var characterData: Character

    private lateinit var characterDetailViewModel: CharacterDetailViewModel

    private lateinit var nameTextView: TextView
    private lateinit var raceTextView: TextView
    private lateinit var strengthTextView: TextView
    private lateinit var wisdomTextView: TextView
    private lateinit var dexterityTextView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        setHasOptionsMenu(true)

        val factory = CharacterDetailViewModelFactory(requireActivity())
        characterDetailViewModel = ViewModelProvider(this, factory).get(CharacterDetailViewModel::class.java)

        val characterId = args.characterId
        characterDetailViewModel.loadCharacter(characterId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        nameTextView = view.findViewById(R.id.name_text_view)
        raceTextView = view.findViewById(R.id.race_text_view)
        strengthTextView = view.findViewById(R.id.strength_text_view)
        wisdomTextView = view.findViewById(R.id.wisdom_text_view)
        dexterityTextView = view.findViewById(R.id.dexterity_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")

        characterDetailViewModel.characterLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { character ->
                character?.let {
                    this.characterData = character
                    updateUI()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.fragment_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(logTag, "onOptionsItemSelected() called")
        return when (item.itemId) {
            R.id.delete_character_menu_item -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.confirm_delete)
                    .setMessage( getString(R.string.confirm_delete_message, characterData.name) )
                    .setIcon(R.drawable.ic_menu_delete_character_light)
                    .setPositiveButton(android.R.string.yes ) { _, _ ->
                        characterDetailViewModel.deleteCharacter(characterData)
                        val action = CharacterDetailFragmentDirections.actionCharacterDetailFragmentToCharacterListFragment()
                        findNavController().navigate(action)
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        Log.d(logTag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(logTag, "onStop() called")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(logTag, "onDestroyView() called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(logTag, "onDetach() called")
        super.onDetach()
    }

    private fun updateUI() {
        characterData.run {
            nameTextView.text = name
            raceTextView.text = race.toLowerCase().capitalize()
            dexterityTextView.text = dex
            wisdomTextView.text = wis
            strengthTextView.text = str
        }
    }
}