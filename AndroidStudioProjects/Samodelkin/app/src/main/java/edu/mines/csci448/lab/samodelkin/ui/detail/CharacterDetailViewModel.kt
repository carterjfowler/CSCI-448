package edu.mines.csci448.lab.samodelkin.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import edu.mines.csci448.lab.samodelkin.data.Character
import edu.mines.csci448.lab.samodelkin.data.database.SamodelkinRepository
import java.util.*

class CharacterDetailViewModel(private val samodelkinRepository: SamodelkinRepository) : ViewModel() {

    private val characterIdLiveData = MutableLiveData<UUID>()

    var characterLiveData: LiveData<Character?> =
        Transformations.switchMap(characterIdLiveData) { characterId ->
            samodelkinRepository.getCharacter(characterId)
        }

    fun loadCharacter(characterId: UUID) {
        characterIdLiveData.value = characterId
    }

    fun deleteCharacter(character: Character) = samodelkinRepository.deleteCharacter(character)

}