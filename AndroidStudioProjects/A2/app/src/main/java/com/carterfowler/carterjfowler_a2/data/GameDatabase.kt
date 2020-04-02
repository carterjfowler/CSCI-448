package com.carterfowler.carterjfowler_a2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "game-database"
@Database(entities = [Game::class], version=1)
@TypeConverters(GameTypeConverters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    //FIX ME: need to either import a SQLite database or figure out how to make a fresh database? Not too sure
    //Think I just need to import a SQLite database
    companion object {
        private var instance: GameDatabase? = null
        fun getInstance(context: Context): GameDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context, GameDatabase::class.java, DATABASE_NAME).build()
            }
        }
    }

}