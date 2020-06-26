package com.vladmakarenko.weatherutils.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.PopupMenuCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.models.LocationModel
import com.vladmakarenko.weatherutils.repository.local.LocationProviderService
import com.vladmakarenko.weatherutils.repository.local.db.WeatherDatabase
import com.vladmakarenko.weatherutils.viewmodels.main.LocationPermissionService
import com.vladmakarenko.weatherutils.viewmodels.main.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val locationClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }

    private lateinit var locale: Locale

    private lateinit var locationPermissionService: LocationPermissionService

    private lateinit var locationProviderService: LocationProviderService

    private val onPermissionsRequestResult = MutableLiveData<Boolean>()

    lateinit var mainViewModel: MainViewModel

    private companion object {
        const val TAG = "MMainActivity"
        const val LOCATION_PERMISSIONS_CODE = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        locale = getLocale()

        locationPermissionService =
            LocationPermissionService(this,
                LOCATION_PERMISSIONS_CODE, onPermissionsRequestResult)
        locationProviderService = LocationProviderService(
            locationClient,
            this
        )

        mainViewModel = MainViewModel(
            requestLocation(),
            this,
            this.applicationContext,
            locale
        )

        //Set up bottom app bar with nav controller
        bottom_app_bar.setupWithNavController(container_fragment.findNavController())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.options_settings -> {
                //TODO:Implement settings menu
                true
            }
            //TODO: Comment out this option
            R.id.options_clear -> {
                GlobalScope.launch(IO){WeatherDatabase(this@MainActivity).clearAllTables()}
                (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSIONS_CODE -> {
                if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                    onPermissionsRequestResult.value = false
                    Log.d(TAG, "Permissions isn\'t accepted already")
                    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
                    recreate()
                    return
                }
                Log.d(TAG, "Permissions is accepted already")
                onPermissionsRequestResult.value = true
                return
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestLocation(): LiveData<LocationModel> {
        progressBar.visibility = View.VISIBLE
        val locationModelLiveData = MutableLiveData<LocationModel>()
        val isAccepted = locationPermissionService.request()
        isAccepted.observe(this, Observer { acceptState ->
            if (acceptState) {
                locationProviderService.getLocation().observe(this, Observer {
                    if (it == null) {
                        buildRequestInternetDialog()
                    } else {
                        progressBar.visibility = View.GONE
                        locationModelLiveData.value = it
                    }
                })
            }
        })
        return locationModelLiveData
    }

    private fun buildRequestInternetDialog() = MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.turn_on_location))
        .setMessage(getString(R.string.turn_on_location_message))
        .setPositiveButton(getString(R.string.ok)) { _, _ -> }

    private fun getLocale() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales.get(0)
    } else {
        resources.configuration.locale
    }
}


