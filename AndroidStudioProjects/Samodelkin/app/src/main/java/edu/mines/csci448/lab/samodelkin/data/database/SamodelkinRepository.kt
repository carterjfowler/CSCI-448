package edu.mines.csci448.lab.samodelkin.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import edu.mines.csci448.lab.samodelkin.data.Character
import java.util.*
import java.util.concurrent.Executors

class SamodelkinRepository private constructor(private val samodelkinDao: SamodelkinDao) {

    companion object {
        private var instance: SamodelkinRepository? = null

        fun getInstance(context: Context): SamodelkinRepository? {
            return instance ?: let {
                if(instance == null) {
                    val database = SamodelkinDatabase.getInstance(context)
                    instance = SamodelkinRepository(database.samodelkinDao())
                }
                return instance
            }
        }
    }

    private val executor = Executors.newSingleThreadExecutor()

    fun getCharacters(): LiveData<PagedList<Character>> {
        return LivePagedListBuilder(
            samodelkinDao.getCharacters(),
            PagedList.Config.Builder().setPageSize(100).build()
        ).build()
    }

    fun getCharacter(id: UUID): LiveData<Character?> = samodelkinDao.getCharacter(id)

    fun addCharacter(character: Character) {
        executor.execute {
            samodelkinDao.addCharacter(character)
        }
    }

    fun deleteCharacter(character: Character) {
        executor.execute {
            samodelkinDao.deleteCharacter(character)
        }
    }

}