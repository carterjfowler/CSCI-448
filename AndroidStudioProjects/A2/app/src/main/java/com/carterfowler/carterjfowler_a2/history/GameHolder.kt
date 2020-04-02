package com.carterfowler.carterjfowler_a2.history

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carterfowler.carterjfowler_a2.R
import com.carterfowler.carterjfowler_a2.data.Game
import kotlinx.android.synthetic.main.history_list_item.view.*

class GameHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var game: Game

    val nameTextView: TextView = itemView.findViewById(R.id.game_name)
    val winnerTextView: TextView = itemView.findViewById(R.id.winner)
    val loserTextView: TextView = itemView.findViewById(R.id.loser)
    val dateTextView: TextView = itemView.findViewById(R.id.date)
    val winningPiece: ImageView = itemView.findViewById(R.id.winning_piece)

    fun bind(game: Game) {
        this.game = game
        if (!this.game.tie) {
            nameTextView.text = this.game.gameName
            var temp = "Winner: " + this.game.winner
            winnerTextView.text = temp
            temp = "Loser: " + this.game.loser
            loserTextView.text = temp
            dateTextView.text = this.game.time.toString()
            if(this.game.winner.equals('x')) {
                winningPiece.setImageResource(R.drawable.ic_x_piece)
            } else if (this.game.winner.equals('o')) {
                winningPiece.setImageResource(R.drawable.ic_o_piece)
            }
        } else {
            nameTextView.text = this.game.gameName
            winnerTextView.text = "Tie"
            dateTextView.text = this.game.time.toString()
            winningPiece.setImageResource(R.drawable.ic_no_piece)
        }
    }
}