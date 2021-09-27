package com.carterjfowler.carterjfowler_a3.Map

import androidx.lifecycle.ViewModel
import com.carterjfowler.carterjfowler_a3.Data.LocationData
import com.carterjfowler.carterjfowler_a3.Data.LocationRepository
import java.util.*

class MapViewModel(private val locationRepository: LocationRepository) : ViewModel() {
    var mapLiveData = locationRepository.getLocations()

    fun addLocation(locationData: LocationData) = locationRepository.addLocation(locationData)

    fun deleteLocation(longitude: Double, latitude: Double, time: Date) = locationRepository.deleteLocation(longitude, latitude, time)

//    fun getLocation(longitude: Double, latitude: Double, time: Date) = locationRepository.getLocation(longitude, latitude, time)

}