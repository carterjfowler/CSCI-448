package edu.mines.csci448.lab.samodelkin.data.database

import androidx.room.TypeConverter
import java.util.*

class SamodelkinTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
}