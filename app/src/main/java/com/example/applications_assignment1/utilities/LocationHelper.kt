package com.example.applications_assignment1.utilities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationHelper(private val activity: Activity) {

    companion object {
        const val REQ_LOCATION = 1001
    }

    private val client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun hasFineLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQ_LOCATION
        )
    }

    fun getLastLocationOrNull(onResult: (Location?) -> Unit) {
        if (!hasFineLocationPermission()) {
            onResult(null)
            return
        }

        client.lastLocation
            .addOnSuccessListener { onResult(it) }
            .addOnFailureListener { onResult(null) }
    }
}
