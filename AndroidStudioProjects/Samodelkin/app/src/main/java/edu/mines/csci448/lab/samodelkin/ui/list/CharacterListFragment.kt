package edu.mines.csci448.lab.samodelkin.ui.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.lab.samodelkin.R
import edu.mines.csci448.lab.samodelkin.data.Character
import java.util.*

class CharacterListFragment : Fragment() {

    private val logTag = "448.CharListFrag"

    private lateinit var characterListViewModel: CharacterListViewModel

    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var adapter: CharacterListAdapter



    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        setHasOptionsMenu(true)

        val factory = CharacterListViewModelFactory(requireContext())
        characterListViewModel = ViewModelProvider(this, factory).get(CharacterListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        characterRecyclerView = view.findViewById(R.id.character_recycler_view)
        characterRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CharacterListAdapter(characterListViewModel) { character: Character -> Unit
            val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment2( character.id )
            findNavController().navigate(action)
        }
        characterRecyclerView.adapter = adapter

        val itemTouchHelperCallback = SwipeToDeleteHelper(adapter)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(characterRecyclerView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")

        characterListViewModel.characterListLiveData.observe(
            viewLifecycleOwner,
            Observer { characters ->
                characters?.let {
                    Log.d(logTag, "Got characters ${characters.size}")
                    updateUI(characters)
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.fragment_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(logTag, "onOptionsItemSelected() called")
        return when (item.itemId) {
            R.id.new_character_menu_item -> {
                val action = CharacterListFragmentDirections.actionCharacterListFragmentToGenerateCharacterFragment()
                findNavController().navigate(action)
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

    private fun updateUI(characters: PagedList<Character>) {
        adapter.submitList(characters)
    }
}