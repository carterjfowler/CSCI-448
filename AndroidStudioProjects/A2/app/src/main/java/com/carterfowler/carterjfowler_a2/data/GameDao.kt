package com.carterfowler.carterjfowler_a2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT MAX(gameNum) FROM game")
    fun getMaxGameNum(): Int

    @Insert
    fun insertGame(game: Game)


}