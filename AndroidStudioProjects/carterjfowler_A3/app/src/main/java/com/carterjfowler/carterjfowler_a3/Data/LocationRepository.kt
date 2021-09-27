package com.carterjfowler.carterjfowler_a3.Data

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.*
import java.util.concurrent.Executors

class LocationRepository (private val locationDao: LocationDao) {
    private val executor = Executors.newSingleThreadExecutor()

//    fun getLocations(): LiveData<List<LocationData>> = locationDao.getLocations()
    fun getLocations(): LiveData<PagedList<LocationData>> {
        return LivePagedListBuilder(
            locationDao.getLocations(),
            PagedList.Config.Builder().setPageSize(100).build()
        ).build()
    }

    fun addLocation(locationData: LocationData) {
        executor.execute {
            locationDao.insertLocation(locationData)
        }
    }

    fun deleteEntries() {
        executor.execute {
            locationDao.deleteEntries()
        }
    }

//    fun getLocation(longitude: Double, latitude: Double, time: Date): LocationData = locationDao.getLocation(longitude, latitude, time)

    fun deleteLocation(longitude: Double, latitude: Double, time: Date) {
        executor.execute {
            val locationData: LocationData = locationDao.getLocation(longitude, latitude, time)
            locationDao.deleteLocation(locationData)
        }
    }

    companion object {
        private var instance: LocationRepository? = null
        fun getInstance(context: Context): LocationRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = LocationDatabase.getInstance(context)
                    instance = LocationRepository(database.locationDao())
                }
                return instance
            }
        }
    }
}