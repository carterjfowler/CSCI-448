package com.carterjfowler.Lab09

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
import androidx.core.app.NotificationCompat


class LocatrWorker(private val context: Context, workerParams: WorkerParameters) : ListenableWorker(context, workerParams) {
    private val logTag = "448.LocatrWorker"

    override fun startWork(): ListenableFuture<Result> {
        Log.d(logTag, "Work triggered")

        return CallbackToFutureAdapter.getFuture { completer ->
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    Log.d(logTag, "Got a location: $location")
//                    val intent = MainActivity.createIntent(context, location).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//                    val notificationManager = NotificationManagerCompat.from(context)
//                    val channelID = context.resources.getString(R.string.notification_channel_id)
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        // version is Oreo or higher
//                        // version is API 26 or higher
//                        // version is Android 8.0 or higher
//                        val channel = NotificationChannel(channelID, R.string.notification_channel_name.toString(), NotificationManager.IMPORTANCE_DEFAULT).apply {
//                            description = context.resources.getString(R.string.notification_channel_desc)
//                        }
//                        notificationManager.createNotificationChannel(channel)
//                        val notification = NotificationCompat.Builder(context, channelID)
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(context.resources.getString(R.string.notification_title))
//                            .setContentText("(${location.latitude}, ${location.longitude})")
//                            .setAutoCancel(true)
//                            .setContentIntent(pendingIntent)
//                            .build()
//                        notificationManager.notify(0, notification)
//                    }
                    completer.set( Result.success() )
                }
            }
        }
    }
}