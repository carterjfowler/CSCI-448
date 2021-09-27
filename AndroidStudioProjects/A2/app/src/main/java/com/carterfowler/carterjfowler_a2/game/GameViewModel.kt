package com.carterfowler.carterjfowler_a2.game

import androidx.lifecycle.ViewModel
import com.carterfowler.carterjfowler_a2.data.Game
import com.carterfowler.carterjfowler_a2.data.GameRepository

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {
    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }
}