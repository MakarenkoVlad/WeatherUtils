package com.vladmakarenko.weatherutils.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vladmakarenko.weatherutils.repository.local.db.WeatherDatabase
import com.vladmakarenko.weatherutils.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class AlarmWorker(appContext: Context, private val params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {
    companion object{
        private const val TAG = "AlarmWorker"
    }
    private val notificationHelper = NotificationHelper(appContext)
    private val alarmDao = WeatherDatabase(appContext).getAlarmDAO()

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: Working")
        val data = params.inputData
        val title = data.getString(AlarmCreatorWorker.ALARM_TITLE)?:""
        val desc = data.getString(AlarmCreatorWorker.ALARM_DESC)?:""
        val icon = data.getInt(AlarmCreatorWorker.ALARM_ICON, 0)
        val key = data.getInt(AlarmCreatorWorker.ALARM_KEY, 0)
        notificationHelper.showNotification(title, desc, icon)
        withContext(IO){alarmDao.deleteByKey(key)}
        return Result.success()
    }
}