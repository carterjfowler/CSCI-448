package edu.mines.csci448.lab.samodelkin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Character(val name: String,
                val race: String,
                val dex: String,
                val wis: String,
                val str: String,
                @PrimaryKey val id: UUID = UUID.randomUUID()) : Serializable