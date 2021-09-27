package com.carterjfowler.carterjfowler_a3.Map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.work.*
import com.carterjfowler.carterjfowler_a3.Data.LocationData
import com.carterjfowler.carterjfowler_a3.Data.LocationTypeConverters

import com.carterjfowler.carterjfowler_a3.R
import com.carterjfowler.carterjfowler_a3.api.WeatherFetcher
import com.carterjfowler.carterjfowler_a3.api.descriptionItem
import com.carterjfowler.carterjfowler_a3.api.tempItem
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.StringBuilder

private const val REQUEST_LOC_PERMISSION = 1
private const val FINE_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
const val ARG_LONGITUDE_ID = "longitude_id"
const val ARG_LATITUDE_ID = "latitude_id"
const val UNKNOWN_POSITION = -999.0
class MapFragment : SupportMapFragment() {
    companion object {
        const val REQUEST_LOC_ON = 0
        const val PERIODIC_POLL_NAME = "periodicPollName"
    }

    private var locationUpdateState = false

    private val logTag = "448.MapFragment"
    val formatter = SimpleDateFormat("EEE, MMM dd, yyyy hh:mm aaa")

    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location

    private lateinit var fab: FloatingActionButton

    private lateinit var snackBar: Snackbar
    private lateinit var snackBarMessage: String

    private lateinit var mapViewModel: MapViewModel

    private lateinit var prefManager : SharedPreferences
    private var saveCheckIns = true

    private val args: MapFragmentArgs by navArgs()

    private var toastText: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag, "onAttach() called")
        prefManager = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate() called")

        val factory = MapViewModelFactory(requireActivity())
        mapViewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)

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
                apiCall(lastLocation)
                updateUI()
            }
        }

        getMapAsync { map ->
            googleMap = map
            requireActivity().invalidateOptionsMenu()
            googleMap.setOnMapLoadedCallback {
                updateUI()
                populateMarkers()
            }
        }

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
        val view = inflater.inflate(R.layout.fragment_locatr, container, false)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            checkPermissionAndGetLocation()
        }
        view.findViewById<FrameLayout>(R.id.mapFrameLayout).addView(mapView)

        snackBarMessage = ""
        snackBar = Snackbar.make(view.findViewById(R.id.coordLayout), snackBarMessage, 5000)

        saveCheckIns = prefManager.getBoolean("save_location_switch", true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(logTag, "onViewCreated() called")
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
                    exc.startResolutionForResult(requireActivity(),
                        REQUEST_LOC_ON
                    )
                } catch (e: IntentSender.SendIntentException) {
                    // do nothing, they cancelled so ignore error
                }
            }
        }
    }

    private fun checkPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                FINE_LOCATION_PERMISSION
            ) != PERMISSION_GRANTED) {
            // permission not granted
            // check if we should ask
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    FINE_LOCATION_PERMISSION
                )) {
                // user already said no, don't ask again
                Toast.makeText(requireContext(),"We must access your location to plot where you are", Toast.LENGTH_LONG).show()
            } else {
                // user hasn't previously declined, ask them
                requestPermissions(listOf(FINE_LOCATION_PERMISSION).toTypedArray(),
                    REQUEST_LOC_PERMISSION
                )
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

        // add the new markers
        googleMap.addMarker(myMarker)
        googleMap.setOnMarkerClickListener {
            val latitude = it.position.latitude
            val longitude = it.position.longitude
            var time = Date(0)
            var temp = 0.0
            var description = ""
            mapViewModel.mapLiveData.observe(
                viewLifecycleOwner,
                Observer { locationData ->
                    locationData.let {
                        for (location in locationData) {
                            if(location.longitude == longitude && location.latitude == latitude) {
                                time = location.time
                                temp = location.temp
                                description = location.weatherDescription
                            }
                        }
                    }
                    if (time != Date(0) && temp != 0.0 && description.isNotEmpty()) {
                        snackBarMessage =
                            "You were here: " + formatter.format(time) + "\nTemp: " + String.format(
                                "%.2f",
                                temp
                            ) + "°F (" + description + ")"
                        snackBar.setText(snackBarMessage)
                        snackBar.setAction("Delete") { view ->
                            //add delete from database action
                            mapViewModel.deleteLocation(longitude, latitude, time)
                            //clear from map
                            it.remove()
                        }
                        snackBar.show()
                    }
                }
            )
            it.showInfoWindow()
            true
        }

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

    private fun apiCall(location: Location) {
        val latitude: Double = location.latitude
        val longitude: Double = location.longitude

        var temp = 0.0
        var description = ""
        val tempLiveData: LiveData<tempItem> = WeatherFetcher().fetchTemp(longitude,latitude)
        tempLiveData.observe(
            this,
            Observer {tempItem ->
                temp = (tempItem.temp - 273.15) * (9/5) + 32
                Log.d(logTag, "Response received, temp = ${String.format("%.2f", temp)}")
                val descriptionLiveData: LiveData<List<descriptionItem>> = WeatherFetcher().fetchDescription(longitude,latitude)
                descriptionLiveData.observe(
                    this,
                    Observer {descriptionItems ->
                        description = descriptionItems[0].description
                        Log.d(logTag, "Response received, description = $description")
                        val time = Date()
                        if (saveCheckIns) { addToDatabase(location, temp, description, time) }
                        //display toast
                        toastText = "Lat/Lng: (" + latitude.toString() + "°, " + longitude.toString() + "°)\nYou were here: " + formatter.format(time) + "\nTemp: " + String.format("%.2f", temp) + "°F (" + description + ")"
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                    }
                )
            }
        )

    }

    private fun addToDatabase(location: Location, temp: Double, description: String, time: Date) {
        val newLocation = LocationData(
            longitude = location.longitude,
            latitude = location.latitude,
            address = getAddress(location),
            time = time,
            temp = temp,
            weatherDescription = description)
        mapViewModel.addLocation(newLocation)
    }

    //Function to populate map with markers from the database
    private fun populateMarkers() {
        mapViewModel.mapLiveData.observe (
            viewLifecycleOwner,
            Observer { locationData ->
                locationData.let {list ->
                    for (location in locationData) {
                        // make sure we have a map and a location
                        if( !::googleMap.isInitialized) {
                            break
                        }
                        // create a point for the corresponding lat/long
                        val myLocationPoint = LatLng(location.latitude, location.longitude)

                        val loc: Location = Location("")
                        loc.latitude = location.latitude
                        loc.longitude = location.longitude

                        // create the marker
                        val myMarker = MarkerOptions()
                            .position(myLocationPoint)
                            .title( getAddress(loc) )

                        // add the new markers
                        googleMap.addMarker(myMarker)
                        googleMap.setOnMarkerClickListener {
                            val latitude = it.position.latitude
                            val longitude = it.position.longitude
                            var time = Date(0)
                            var temp = 0.0
                            var description = ""
                            mapViewModel.mapLiveData.observe(
                                viewLifecycleOwner,
                                Observer { locationData1 ->
                                    locationData1.let {
                                        for (location1 in locationData1) {
                                            if(location1.longitude == longitude && location1.latitude == latitude) {
                                                time = location1.time
                                                temp = location1.temp
                                                description = location1.weatherDescription
                                            }
                                        }
                                    }
                                    if (time != Date(0) && temp != 0.0 && description.isNotEmpty()) snackBarMessage = "You were here: " + formatter.format(time) + "\nTemp: " + String.format("%.2f", temp) + "°F (" + description + ")"
                                    snackBar.setText(snackBarMessage)
                                    snackBar.setAction("Delete") {view ->
                                        //add delete from database action
                                        mapViewModel.deleteLocation(longitude, latitude, time)
                                        //clear from map
                                        it.remove()
                                    }
                                    snackBar.show()
                                }
                            )
                            it.showInfoWindow()
                            true
                        }
                    }
                }
            }
        )
    }
}