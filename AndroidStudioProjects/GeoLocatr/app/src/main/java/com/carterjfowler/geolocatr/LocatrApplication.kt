package com.carterjfowler.geolocatr

import android.app.Application

class LocatrAppliction : Application() {
    private val logTag = "448.LocatrAppliction"

    companion object {
        lateinit var locatrSharedPreferences: LocatrPreferences
    }

    override fun onCreate() {
        super.onCreate()
        locatrSharedPreferences = LocatrPreferences(applicationContext)
    }
}