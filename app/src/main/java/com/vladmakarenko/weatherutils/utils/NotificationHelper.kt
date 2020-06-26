package com.vladmakarenko.weatherutils.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vladmakarenko.weatherutils.R
import kotlin.random.Random

class NotificationHelper(private val appContext: Context) {
    private companion object {
        const val CHANNEL_ID = "weatherAlarmChannel"
        const val ID_BOUND = 10000
    }

    init {
        buildNotificationChannel()
    }

    fun showNotification(title: String, description: String, iconId: Int) {
        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(iconId)
            .setContentTitle(title)
            .setContentText(description)
            .build()

        NotificationManagerCompat.from(appContext)
            .notify(Random.nextInt(ID_BOUND), notification)
    }

    private fun buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = appContext.getString(R.string.notification_channel_name)
            val description = appContext.getString(R.string.notification_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}