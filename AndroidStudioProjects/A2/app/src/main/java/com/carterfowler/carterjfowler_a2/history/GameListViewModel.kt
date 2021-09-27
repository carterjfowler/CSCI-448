package com.carterfowler.carterjfowler_a2.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.carterfowler.carterjfowler_a2.data.Game
import com.carterfowler.carterjfowler_a2.data.GameRepository

class GameListViewModel(private val gameRepository: GameRepository) : ViewModel() {
    var gameListLiveData = gameRepository.getGames()

    fun deleteEntries() {
        gameRepository.deleteEntries()
    }
}