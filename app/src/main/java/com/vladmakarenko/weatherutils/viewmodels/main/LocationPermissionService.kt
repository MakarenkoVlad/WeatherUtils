package com.vladmakarenko.weatherutils.viewmodels.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vladmakarenko.weatherutils.R
import kotlin.system.exitProcess

// onPermissionRequestResult - live data from onRequestPermissionResult
class LocationPermissionService(
    private val activity: AppCompatActivity,
    private val code: Int,
    private val onPermissionRequestResult: LiveData<Boolean>
) {
    private val context = activity.baseContext
    private val appContext = activity.applicationContext
    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    //Return live data with boolean[true - if permission granted, otherwise - false]
    @SuppressLint("NewApi")
    fun request(): LiveData<Boolean> {
        val isPermissionGranted = MutableLiveData<Boolean>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && PERMISSIONS.any {
                activity.checkSelfPermission(
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            buildPermissionDialog()
            onPermissionRequestResult.observe(
                activity,
                Observer { isPermissionGranted.value = it })
        } else{
            isPermissionGranted.value = true
        }
        return isPermissionGranted
    }

    @SuppressLint("NewApi")
    private fun buildPermissionDialog() = MaterialAlertDialogBuilder(activity)
        .setTitle(appContext.getString(R.string.permission))
        .setMessage(appContext.getString(R.string.location_permission_description))
        .setPositiveButton(appContext.getString(R.string.ok)) { _, _ ->
            activity.requestPermissions(
                PERMISSIONS,
                code
            )
        }
        .setNegativeButton(appContext.getString(R.string.no)) { _, _ -> exitProcess(0) }
        .show()
}