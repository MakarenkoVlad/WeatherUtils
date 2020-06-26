package com.vladmakarenko.weatherutils

import android.app.Application
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.work.*
import com.vladmakarenko.weatherutils.repository.local.db.WeatherDatabase
import com.vladmakarenko.weatherutils.workers.AlarmCreatorWorker
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {
        private const val TAG = "App"
        private const val PERIODIC_ALARM_REQUEST = "Periodic alarm request"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Working")
        val db = WeatherDatabase(this)
        val request = OneTimeWorkRequestBuilder<AlarmCreatorWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()
        val periodicRequest = PeriodicWorkRequestBuilder<AlarmCreatorWorker>(1, TimeUnit.DAYS)
            .build()
        with(WorkManager.getInstance(this)) {
            enqueueUniquePeriodicWork(
                PERIODIC_ALARM_REQUEST,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        }
//    TODO: Make to request from internet
    }
}