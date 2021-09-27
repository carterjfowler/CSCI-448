package com.carterfowler.carterjfowler_a2.data

import android.content.Context
import androidx.lifecycle.LiveData
import java.util.concurrent.Executors

class GameRepository(private val gameDao: GameDao) {
    private val executor = Executors.newSingleThreadExecutor()

    fun getGames(): LiveData<List<Game>> = gameDao.getGames()

    fun addGame(game: Game) {
        executor.execute {
            gameDao.insertGame(game)
        }
    }

    fun deleteEntries() {
        executor.execute {
            gameDao.deleteEntries()
        }
    }

    companion object {
        private var instance: GameRepository? = null
        fun getInstance(context: Context): GameRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = GameDatabase.getInstance(context)
                    instance = GameRepository(database.gameDao())
                }
                return instance
            }
        }
    }
}