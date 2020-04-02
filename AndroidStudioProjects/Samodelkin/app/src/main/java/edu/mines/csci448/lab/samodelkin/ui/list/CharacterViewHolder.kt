package edu.mines.csci448.lab.samodelkin.ui.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.mines.csci448.lab.samodelkin.R
import edu.mines.csci448.lab.samodelkin.data.Character

class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var character: Character

    private val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
    private val raceTextView: TextView = itemView.findViewById(R.id.race_text_view)
    private val strengthTextView: TextView = itemView.findViewById(R.id.strength_text_view)
    private val wisdomTextView: TextView = itemView.findViewById(R.id.wisdom_text_view)
    private val dexterityTextView: TextView = itemView.findViewById(R.id.dexterity_text_view)

    fun bind(character: Character, clickListener: (Character) -> Unit ) {
        this.character = character
        itemView.setOnClickListener { clickListener(this.character) }

        this.character.run {
            nameTextView.text = name
            raceTextView.text = race.toLowerCase().capitalize()
            dexterityTextView.text = dex
            wisdomTextView.text = wis
            strengthTextView.text = str
        }
    }

    fun clear() {
        nameTextView.text = ""
        raceTextView.text = ""
        dexterityTextView.text = ""
        wisdomTextView.text = ""
        strengthTextView.text = ""
    }

}