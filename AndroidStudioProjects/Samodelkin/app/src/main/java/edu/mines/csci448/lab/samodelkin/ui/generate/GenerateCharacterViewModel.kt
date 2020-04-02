package edu.mines.csci448.lab.samodelkin.ui.generate

import androidx.lifecycle.ViewModel
import edu.mines.csci448.lab.samodelkin.data.Character
import edu.mines.csci448.lab.samodelkin.data.database.SamodelkinRepository

class GenerateCharacterViewModel(private val samodelkinRepository: SamodelkinRepository) : ViewModel() {

    fun addCharacter(character: Character) = samodelkinRepository.addCharacter(character)

}