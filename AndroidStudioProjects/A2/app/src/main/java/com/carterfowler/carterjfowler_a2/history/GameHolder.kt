package com.carterfowler.carterjfowler_a2.history

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carterfowler.carterjfowler_a2.R
import com.carterfowler.carterjfowler_a2.data.Game

class GameHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var game: Game

    val winnerTextView: TextView = itemView.findViewById(R.id.winner_name)
    val loserTextView: TextView = itemView.findViewById(R.id.loser_name)
    val dateTextView: TextView = itemView.findViewById(R.id.date)
    val winningPiece: ImageView = itemView.findViewById(R.id.winning_piece)
    val tieTitle: TextView = itemView.findViewById(R.id.tie_title)
    val winnerTitle: TextView = itemView.findViewById(R.id.winner_title)
    val loserTitle: TextView = itemView.findViewById(R.id.loser_title)

    fun bind(game: Game) {
        this.game = game
        if (!this.game.tie) {
            winnerTextView.text = this.game.winner
            loserTextView.text = this.game.loser
            dateTextView.text = this.game.time.toString()
            if(this.game.piece.equals('x')) {
                winningPiece.setImageResource(R.drawable.ic_x_piece)
            } else if (this.game.piece.equals('o')) {
                winningPiece.setImageResource(R.drawable.ic_o_piece)
            }
            tieTitle.visibility = View.INVISIBLE
            winnerTitle.visibility = View.VISIBLE
            loserTitle.visibility = View.VISIBLE
        } else {
            winnerTextView.text = this.game.winner
            loserTextView.text = this.game.loser
            dateTextView.text = this.game.time.toString()
            winningPiece.setImageResource(R.drawable.ic_no_piece)
            tieTitle.visibility = View.VISIBLE
            winnerTitle.visibility = View.INVISIBLE
            loserTitle.visibility = View.INVISIBLE
        }
    }
}