package com.carterfowler.carterjfowler_a2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getGames(): LiveData<List<Game>>

    @Insert
    fun insertGame(game: Game)

    @Query("DELETE FROM game")
    fun deleteEntries()
}