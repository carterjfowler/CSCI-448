package edu.mines.csci448.lab.samodelkin.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.lab.samodelkin.R
import edu.mines.csci448.lab.samodelkin.data.Character

class CharacterListAdapter(private val characterListViewModel: CharacterListViewModel, private val clickListener: (Character) -> Unit ) :
    PagedListAdapter<Character, CharacterViewHolder>(DIFF_UTIL),
    SwipeToDeleteHelper.ItemTouchHelperAdapter {

    private lateinit var attachedRecyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        if(character != null ) {
            holder.bind(character, clickListener)
        } else {
            holder.clear()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedRecyclerView = recyclerView
    }

    override fun onItemDismiss(position: Int) {
        val context = attachedRecyclerView.context
        val character = getItem(position)
        if (character != null) {
            AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete)
                .setMessage(context.resources.getString(R.string.confirm_delete_message, character.name))
                .setIcon(R.drawable.ic_menu_delete_character_light)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    characterListViewModel.deleteCharacter(character)
                }
                .setNegativeButton(android.R.string.no) { _, _ -> notifyItemChanged(position) }
                .show()
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Character>() {
            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean = oldItem == newItem

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean = oldItem.id == newItem.id
        }
    }
}