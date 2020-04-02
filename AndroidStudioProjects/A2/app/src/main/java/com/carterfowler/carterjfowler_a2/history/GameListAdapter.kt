package com.carterfowler.carterjfowler_a2.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carterfowler.carterjfowler_a2.R
import com.carterfowler.carterjfowler_a2.data.Game

class GameListAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameHolder>() {

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return GameHolder(view)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
    }

}