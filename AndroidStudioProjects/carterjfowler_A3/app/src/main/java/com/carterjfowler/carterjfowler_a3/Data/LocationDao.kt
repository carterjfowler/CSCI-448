package com.carterjfowler.carterjfowler_a3.Data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*


@Dao
interface LocationDao {
    @Query("SELECT * FROM locationData")
    fun getLocations(): DataSource.Factory<Int, LocationData>

    @Query("SELECT * FROM locationData WHERE longitude=(:longitude) AND latitude=(:latitude) AND time=(:time)")
    fun getLocation(longitude: Double, latitude: Double, time: Date): LocationData

    @Insert
    fun insertLocation(locationData: LocationData)

    @Delete
    fun deleteLocation(locationData: LocationData)

    @Query ("DELETE FROM locationData")
    fun deleteEntries()
}