package edu.mines.csci448.lab.samodelkin.data.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import edu.mines.csci448.lab.samodelkin.data.Character
import java.util.*

@Dao
interface SamodelkinDao {

    @Query("SELECT * FROM character")
    fun getCharacters(): DataSource.Factory<Int, Character>

    @Query("SELECT * FROM character WHERE id=(:id)")
    fun getCharacter(id: UUID): LiveData<Character?>

    @Insert
    fun addCharacter(character: Character)

    @Delete
    fun deleteCharacter(character: Character)

}