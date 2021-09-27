package com.carterjfowler.carterjfowler_a3.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "location-database"

@Database(entities = [ LocationData::class ], version=1)
@TypeConverters(LocationTypeConverters::class)
abstract class LocationDatabase : RoomDatabase() {

    companion object {
        private var instance: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context, LocationDatabase::class.java, DATABASE_NAME).build()
            }
        }
    }

    abstract fun locationDao(): LocationDao

}