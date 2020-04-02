package com.carterfowler.carterjfowler_a2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Game(@PrimaryKey val gameName: String = "",
    var gameNum: Int,
    var winner: String = "",
    var loser: String = "",
    var tie: Boolean,
    var piece: Char,
    var time: Date = Date())