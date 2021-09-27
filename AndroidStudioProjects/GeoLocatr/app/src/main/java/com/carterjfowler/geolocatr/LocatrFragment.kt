package com.carterjfowler.geolocatr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.text.StringBuilder

private const val REQUEST_LOC_PERMISSION = 1
private const val FINE_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
const val ARG_LONGITUDE_ID = "longitude_id"
const val ARG_LATITUDE_ID = "latitude_id"
const val UNKNOWN_POSITION = -999.0
class LocatrFragment : SupportMapFragment() {
    companion object {
        const val REQUEST_LOC_ON = 0
        const val PERIODIC_POLL_NAME = "periodicPollName"
    }

    private var locationUpdateState = false

    private val logTag = "448.LocatrFragment"

    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location

    private lateinit var workManager: WorkManager

    private val args: LocatrFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")
        setHasOptionsMenu(true)

        locationRequest = LocationRequest.create()
        locationRequest.interval = 0
        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d(logTag, "Got a location: ${locationResult?.lastLocation}")
                lastLocation = locationResult.lastLocation
                updateUI()
            }
        }

        getMapAsync { map ->
            googleMap = map
            requireActivity().invalidateOptionsMenu()
            googleMap.setOnMapLoadedCallback {
                updateUI()
            }
        }

        workManager = WorkManager.getInstance(requireContext())

        val latitude: Double = args.latitude.toDouble()
        val longitude: Double = args.longitude.toDouble()
        if(latitude != UNKNOWN_POSITION && longitude != UNKNOWN_POSITION) {
            lastLocation = Location("") // we need to provide a location provider,
                                        // which is ignored
            lastLocation.latitude = latitude
            lastLocation.longitude = longitude
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(logTag, "onCreateView() called")
        val mapView = super.onCreateView(inflater, container, savedInstanceState)
        return mapView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(logTag, "onCreateOptionsMenu() called")
        inflater.inflate(R.menu.fragment_locatr, menu)

        val locationItem = menu?.findItem(R.id.get_location_menu_item)
        locationItem.isEnabled = (locationUpdateState && ::googleMap.isInitialized)

        val pollingItem = menu.findItem(R.id.polling_menu_item)
        pollingItem.title =
            if (LocatrAppliction.locatrSharedPreferences.isPollingOn) {
                getString( R.string.end_polling_label )
            } else {
                getString( R.string.start_polling_label )
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(logTag, "onOptionsItemSelected() called")
        return when (item.itemId) {
            R.id.get_location_menu_item -> {
                Log.d(logTag, "Get_Location_Menu_Item selected")
                checkPermissionAndGetLocation()
                true
            }
            R.id.polling_menu_item -> {
                Log.d(logTag, "Polling_Menu_Item selected")
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
                val periodicWorkRequest =
                    PeriodicWorkRequestBuilder<LocatrWorker>(1, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build()
                LocatrAppliction.locatrSharedPreferences.isPollingOn =
                    !LocatrAppliction.locatrSharedPreferences.isPollingOn
                requireActivity().invalidateOptionsMenu()
                if (LocatrAppliction.locatrSharedPreferences.isPollingOn) {
                    workManager.enqueueUniquePeriodicWork(PERIODIC_POLL_NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicWorkRequest
                    )
                } else {
                    workManager.cancelUniqueWork(PERIODIC_POLL_NAME)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(logTag, "onActivityCreated() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(logTag, "onActivityResult() called")
        if(resultCode != Activity.RESULT_OK) {
            return
        }
        if(requestCode == REQUEST_LOC_ON) {
            locationUpdateState = true
            requireActivity().invalidateOptionsMenu()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "onStart() called")

        checkIfLocationCanBeRetrieved()
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "onResume() called")
    }

    override fun onPause() {
        Log.d(logTag, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(logTag, "onStop() called")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(logTag, "onDestroyView() called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy() called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(logTag, "onDetach() called")
        super.onDetach()
    }

    private fun checkIfLocationCanBeRetrieved() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            locationUpdateState = true
            requireActivity().invalidateOptionsMenu()
        }
        task.addOnFailureListener {exc ->
            locationUpdateState = false
            requireActivity().invalidateOptionsMenu()
            if(exc is ResolvableApiException) {
                try {
                    exc.startResolutionForResult(requireActivity(), REQUEST_LOC_ON)
                } catch (e: IntentSender.SendIntentException) {
                    // do nothing, they cancelled so ignore error
                }
            }
        }
    }

    private fun checkPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), FINE_LOCATION_PERMISSION) != PERMISSION_GRANTED) {
            // permission not granted
            // check if we should ask
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), FINE_LOCATION_PERMISSION)) {
                // user already said no, don't ask again
                Toast.makeText(requireContext(),"We must access your location to plot where you are", Toast.LENGTH_LONG).show()
            } else {
                // user hasn't previously declined, ask them
                requestPermissions(listOf(FINE_LOCATION_PERMISSION).toTypedArray(), REQUEST_LOC_PERMISSION)
            }
        } else {
            // permission has been granted, do what we want
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOC_PERMISSION) {
            if (permissions.isNotEmpty() && permissions[0] == FINE_LOCATION_PERMISSION
                && grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                checkPermissionAndGetLocation()
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun getAddress(location: Location): String {
        val geocoder = Geocoder(requireActivity())
        val addressTextBuilder = StringBuilder()
        try {
            val addresses = geocoder.getFromLocation(location.latitude,
                location.longitude,
                1)
            if(addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                for(i in 0..address.maxAddressLineIndex) {
                    if(i > 0) {
                        addressTextBuilder.append( "\n" )
                    }
                    addressTextBuilder.append( address.getAddressLine(i) )
                }
            }
        } catch (e: IOException) {
            Log.e(logTag, e.localizedMessage)
        }
        return addressTextBuilder.toString()
    }

    private fun updateUI() {
        // make sure we have a map and a location
        if( !::googleMap.isInitialized || !::lastLocation.isInitialized ) {
            return
        }
        // create a point for the corresponding lat/long
        val myLocationPoint = LatLng(lastLocation.latitude, lastLocation.longitude)

        // create the marker
        val myMarker = MarkerOptions()
            .position(myLocationPoint)
            .title( getAddress(lastLocation) )
        // clear any prior markers on the map
        googleMap.clear()
        // add the new markers
        googleMap.addMarker(myMarker)

        // convex hull
        val bounds = LatLngBounds.Builder()
            .include(myLocationPoint)
            .build()
        // add a margin
        val margin = resources.getDimensionPixelSize(R.dimen.map_inset_margin)
        // create a camera to smoothly move the map view
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, margin)
        // move our camera!
        googleMap.animateCamera(cameraUpdate)
    }

}