package edu.mines.csci448.lab.samodelkin.util

import edu.mines.csci448.lab.samodelkin.data.Character

private fun <T> List<T>.rand() = shuffled().first()

private fun Int.roll(sides: Int = 6) = (0 until this)
    .map { (1..sides).toList().rand() }
    .sum()
    .toString()

private val firstNames = listOf("Eli", "Alex", "Sophie", "Thorin", "Bilbo", "Fred", "Thorn")
private val lastNames = listOf("Lightweaver", "Greatfoot", "Oakenfeld", "Oakenshield", "Baggins")
private val races = listOf("dwarf", "elf", "human", "halfling", "gnome", "troll", "goblin", "kobold", "ent")

object CharacterGenerator {
    private fun name() = "${firstNames.rand()} ${lastNames.rand()}"
    private fun race() = races.rand()
    private fun dex() = 4.roll(7)
    private fun wis() = 3.roll(8)
    private fun str() = 5.roll(6)

    fun placeHolderCharacter() = Character(
        "Generating Character...",
        "",
        "",
        "",
        ""
    )

    fun generate() = Character(
        name = name(),
        race = race(),
        dex = dex(),
        wis = wis(),
        str = str()
    )

    fun fromApiData(apiData: String): Character {
        val (race, name, dex, wis, str) = apiData.split(",")
        return Character(
            name,
            race,
            dex,
            wis,
            str
        )
    }
}