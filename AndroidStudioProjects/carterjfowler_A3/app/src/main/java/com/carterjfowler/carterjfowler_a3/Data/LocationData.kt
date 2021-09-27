package com.carterjfowler.carterjfowler_a3.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class LocationData(val longitude: Double,
                        val latitude: Double,
                        val address: String,
                        val time: Date,
                        val temp: Double,
                        val weatherDescription: String,
                        @PrimaryKey val id: UUID = UUID.randomUUID()) : Serializable