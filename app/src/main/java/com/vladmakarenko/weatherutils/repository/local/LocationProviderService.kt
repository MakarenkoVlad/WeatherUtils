package com.vladmakarenko.weatherutils.repository.local

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.vladmakarenko.weatherutils.models.LocationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationProviderService(
    private val locationClient: FusedLocationProviderClient,
    private val lifecycleOwner: LifecycleOwner
) {

    fun getLocation(): LiveData<LocationModel?> {
        val mutableLiveLocation = MutableLiveData<LocationModel>()
        CoroutineScope(IO).launch {
            getLastLocation()?.let {
                launch(Dispatchers.Main) {
                    mutableLiveLocation.postValue(
                        LocationModel(
                            lat = it.latitude.toString(),
                            lon = it.longitude.toString()
                        )
                    )
                }
            }
            mutableLiveLocation.postValue(null)
            launch(Dispatchers.Main) {
                getLocationFromGPS().observe(lifecycleOwner, Observer {
                    mutableLiveLocation.value = it
                })
            }
        }
        return mutableLiveLocation
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation() = locationClient.lastLocation.await()

    @SuppressLint("MissingPermission")
    private fun getLocationFromGPS(): LiveData<LocationModel> {
        val mLocationLiveData = MutableLiveData<LocationModel>()

        val request = LocationRequest().apply {
            interval = 10000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(results: LocationResult?) {
                super.onLocationResult(results)

                locationClient.removeLocationUpdates(this)

                results?.let {
                    val location = results.lastLocation
                    val mLocationModel = LocationModel(
                        lon = location.longitude.toString(),
                        lat = location.latitude.toString()
                    )
                    mLocationLiveData.value = mLocationModel
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
        return mLocationLiveData
    }
}