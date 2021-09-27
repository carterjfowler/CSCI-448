package com.carterfowler.carterjfowler_a2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val DATABASE_NAME = "game-database"
@Database(entities = [Game::class], version=2)
@TypeConverters(GameTypeConverters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        private var instance: GameDatabase? = null
        fun getInstance(context: Context): GameDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context, GameDatabase::class.java, DATABASE_NAME).build()
            }
        }
    }

}