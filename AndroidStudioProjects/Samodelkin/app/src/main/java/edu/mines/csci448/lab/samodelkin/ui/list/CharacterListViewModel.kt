package edu.mines.csci448.lab.samodelkin.ui.list

import androidx.lifecycle.ViewModel
import edu.mines.csci448.lab.samodelkin.data.Character
import edu.mines.csci448.lab.samodelkin.data.database.SamodelkinRepository

class CharacterListViewModel(private val samodelkinRepository: SamodelkinRepository) : ViewModel() {

    val characterListLiveData = samodelkinRepository.getCharacters()

    fun deleteCharacter(character: Character) = samodelkinRepository.deleteCharacter(character)

}