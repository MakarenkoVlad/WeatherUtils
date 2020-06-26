package com.vladmakarenko.weatherutils.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.vladmakarenko.weatherutils.models.AlarmModel
import com.vladmakarenko.weatherutils.models.forecast.CertainForecast
import com.vladmakarenko.weatherutils.repository.local.db.AlarmModelDAO
import com.vladmakarenko.weatherutils.repository.local.db.WeatherDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmCreatorWorker(private val appContext: Context, private val params: WorkerParameters) :
    CoroutineWorker(
        appContext,
        params
    ) {

    companion object {
        const val ALARM_TITLE = "title"
        const val ALARM_DESC = "description"
        const val ALARM_ICON = "icon"
        const val ALARM_KEY = "key"
        private const val TAG = "AlarmCreatorWorker"
        private const val MINUTE_IN_MILLIS = 1000 * 60L
        private const val HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60

    }

    override suspend fun doWork(): Result = withContext(IO) {
        return@withContext try {
            val db = WeatherDatabase(appContext)
            val alarmDao = db.getAlarmDAO()
            val forecastDao = db.getForecastDAO()
            val workManager = WorkManager.getInstance(appContext)

            val alarms = alarmDao.getAllAsync()
            val forecast = forecastDao.get()
            if (forecast == null) {
                Log.d(TAG, "doWork: forecast:null")
                Result.failure()
            } else {
                Log.d(TAG, "doWork: forecast:notnull")
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                val time = calendar.timeInMillis
                alarms
                    .filter { !it.isScheduled }.apply { Log.d(TAG, "After filter: $size") }
                    .forEach { alarm ->
                        val weatherIcon = alarm.icon
                        val timeToAlarm =
                            alarm.minutes * MINUTE_IN_MILLIS + alarm.hours * HOUR_IN_MILLIS
                        Log.d(TAG, "createAlarmWorkerRequest: Hour ${alarm.hours}, minutes: ${alarm.minutes}")
                        Log.d(TAG, "createAlarmWorkerRequest: ${forecast.list.size}")
                        forecast.list.firstOrNull { it.weather[0].icon == weatherIcon.first && time + timeToAlarm < it.dt }
                            ?.let {
                                Log.d(TAG, "doWork: creating request")
                                workManager.enqueue(
                                    createAlarmWorkerRequest(
                                        it,
                                        alarm,
                                        calendar,
                                        timeToAlarm,
                                        alarmDao
                                    )
                                )
                            }
                    }

                Result.success()
            }

        } catch (e: Throwable) {
            Log.d(TAG, "doWork: exception: ${e.message}")
            Result.failure()
        }
    }

    private fun createAlarmWorkerRequest(
        forecast: CertainForecast,
        alarm: AlarmModel,
        calendar: Calendar,
        timeToAlarm: Long,
        alarmDao: AlarmModelDAO
    ): OneTimeWorkRequest {
        val requestBuilder = OneTimeWorkRequestBuilder<AlarmWorker>()
        val timeDelay = forecast.dt - calendar.timeInMillis - timeToAlarm
        val timeOnAlarm = forecast.dt - timeToAlarm
        alarm.apply {
            isScheduled = true
            time = timeOnAlarm
            CoroutineScope(IO).launch {
                alarmDao.update(this@apply)
            }
        }

        val data = workDataOf(
            ALARM_TITLE to alarm.title,
            ALARM_DESC to alarm.description,
            ALARM_ICON to alarm.icon.second,
            ALARM_KEY to alarm.key
        )

        Log.d(TAG, "createAlarmWorkerRequest: created request, ${timeDelay / 1000 / 60}")
        return requestBuilder
            .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(alarm.key.toString())
            .build()

    }
}
