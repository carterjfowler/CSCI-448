package edu.mines.csci448.lab.samodelkin.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.mines.csci448.lab.samodelkin.data.Character

private const val DATABASE_NAME = "samodelkin-database"

@Database(entities = [ Character::class ], version=1)
@TypeConverters(SamodelkinTypeConverters::class)
abstract class SamodelkinDatabase : RoomDatabase() {

    companion object {
        private var instance: SamodelkinDatabase? = null

        fun getInstance(context: Context): SamodelkinDatabase {
            return instance ?: let {
                instance ?: Room.databaseBuilder(context, SamodelkinDatabase::class.java, DATABASE_NAME)
                    .createFromAsset("databases/initial-characters.db")
                    .build()
            }
        }
    }

    abstract fun samodelkinDao(): SamodelkinDao

}