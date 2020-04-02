package com.carterfowler.carterjfowler_a2.history

import androidx.lifecycle.ViewModel
import com.carterfowler.carterjfowler_a2.data.Game
import com.carterfowler.carterjfowler_a2.data.GameRepository

class GameListViewModel(private val gameRepository: GameRepository) : ViewModel() {
    val gameListLiveData = gameRepository.getGames()

    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }
}