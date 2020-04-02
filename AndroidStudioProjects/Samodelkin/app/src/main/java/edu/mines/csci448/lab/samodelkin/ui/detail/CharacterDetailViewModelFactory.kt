package edu.mines.csci448.lab.samodelkin.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.mines.csci448.lab.samodelkin.data.database.SamodelkinRepository

class CharacterDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SamodelkinRepository::class.java)
            .newInstance(SamodelkinRepository.getInstance(context))
    }

}