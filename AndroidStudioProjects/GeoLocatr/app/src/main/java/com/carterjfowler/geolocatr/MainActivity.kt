package com.carterjfowler.geolocatr

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == LocatrFragment.REQUEST_LOC_ON) {
            val locatrFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.
                childFragmentManager?.fragments?.get(0) as LocatrFragment?
            locatrFragment?.onActivityResult(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
