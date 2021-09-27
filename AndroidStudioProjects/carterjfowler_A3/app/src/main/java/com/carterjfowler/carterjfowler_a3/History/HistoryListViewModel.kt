package com.carterjfowler.carterjfowler_a3.History

import androidx.lifecycle.ViewModel
import com.carterjfowler.carterjfowler_a3.Data.LocationData
import com.carterjfowler.carterjfowler_a3.Data.LocationRepository
import java.util.*

class HistoryListViewModel(private val locationRepository: LocationRepository) : ViewModel() {
    val locationListLiveData = locationRepository.getLocations()

    fun deleteLocation(longitude: Double, latitude: Double, time: Date) = locationRepository.deleteLocation(longitude, latitude, time)

    fun deleteEntries() {
        locationRepository.deleteEntries()
    }
}