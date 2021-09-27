package com.carterjfowler.geolocatr

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.common.util.concurrent.ListenableFuture
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder


class LocatrWorker(private val context: Context, workerParams: WorkerParameters) : ListenableWorker(context, workerParams) {
    private val logTag = "448.LocatrWorker"

    override fun startWork(): ListenableFuture<Result> {
        Log.d(logTag, "Work triggered")

        return CallbackToFutureAdapter.getFuture { completer ->
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    Log.d(logTag, "Got a location: $location")
                    //use deeplinks
                    val arguments = Bundle().apply {
                        putFloat("longitude", location.longitude.toFloat())
                        putFloat("latitude", location.latitude.toFloat())
                    }
                    val pendingIntent = NavDeepLinkBuilder(context)
                        .setGraph(R.navigation.nav_graph)
                        .setDestination(R.id.locatrFragment)
                        .setArguments(arguments)
                        .createPendingIntent()
                    val notificationManager = NotificationManagerCompat.from(context)
                    val channelID = context.resources.getString(R.string.notification_channel_id)
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // version is Oreo or higher
                        // version is API 26 or higher
                        // version is Android 8.0 or higher
                        val channel = NotificationChannel(channelID, R.string.notification_channel_name.toString(), NotificationManager.IMPORTANCE_DEFAULT).apply {
                            description = context.resources.getString(R.string.notification_channel_desc)
                        }
                        notificationManager.createNotificationChannel(channel)
                        val notification = NotificationCompat.Builder(context, channelID)
                            .setSmallIcon(R.drawable.ic_notification_icon)
                            .setContentTitle(context.resources.getString(R.string.notification_title))
                            .setContentText("(${location.latitude}, ${location.longitude})")
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build()
                        notificationManager.notify(0, notification)
                    }
                    completer.set( Result.success() )
                }
            }
        }
    }
}