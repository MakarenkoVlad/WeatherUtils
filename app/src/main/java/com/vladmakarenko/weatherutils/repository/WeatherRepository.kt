package com.vladmakarenko.weatherutils.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladmakarenko.weatherutils.models.AlarmModel
import com.vladmakarenko.weatherutils.models.LocationModel
import com.vladmakarenko.weatherutils.models.currentforecast.CurrentWeather
import com.vladmakarenko.weatherutils.models.forecast.ForecastModel
import com.vladmakarenko.weatherutils.repository.local.db.WeatherDatabase
import com.vladmakarenko.weatherutils.repository.remote.RetrofitImpl
import com.vladmakarenko.weatherutils.viewmodels.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class WeatherRepository(
    private val locale: Locale,
    private val context: Context
) {

    companion object {
        private const val TAG = "WeatherRepository"
    }

    private val database = WeatherDatabase(context)

    private val alarmDAO = database.getAlarmDAO()
    private val forecastDAO = database.getForecastDAO()

    private val forecastService = RetrofitImpl.forecastService

    private val mCurrentWeatherLiveData = MutableLiveData<CurrentWeather>()
    val currentWeatherLiveData: LiveData<CurrentWeather>
        get() = mCurrentWeatherLiveData

    private val mForecastLiveData = MutableLiveData<ForecastModel>()
    val forecastLiveData: LiveData<ForecastModel>
        get() = mForecastLiveData

    lateinit var location: LocationModel

    fun getWeather() {
        CoroutineScope(IO).launch {
            val response = forecastService.getWeather(
                lat = location.lat,
                lon = location.lon,
                lang = locale.country
            ).await()

            if (response.isSuccessful) {
                response.body()?.let {
                    mCurrentWeatherLiveData.postValue(it)
                }
            }
        }
    }

    fun getForecast() {
        CoroutineScope(IO).launch {
            val forecast = forecastDAO.get()
            if (forecast == null || forecast.list[0].dt * 1000 < Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis) {
                Log.d(TAG, "getForecast: Get from internet, Forecast time: ${forecast}.list[7].dt}, My time: ${Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis}")
                val response = forecastService.getForecast(
                    lat = location.lat,
                    lon = location.lon,
                    lang = locale.country
                ).await()

                if (response.isSuccessful) {
                    response.body()?.let {
                        val tempListItem = it.list[0]
                        tempListItem.dt *= 1000

                        it.list.forEachIndexed { index, certainForecast ->
                            certainForecast.dt = tempListItem.dt + MainViewModel.HOURx3 * index
                        }
                        mForecastLiveData.postValue(it)
                        forecastDAO.set(it)
                    }
                }
                return@launch
            } else {
                Log.d(TAG, "getForecast: Get from db")
                mForecastLiveData.postValue(forecast)
            }
        }
    }

    fun addAlarm(alarmModel: AlarmModel) = CoroutineScope(IO).launch { alarmDAO.insert(alarmModel) }

    fun deleteAlarm(alarmModel: AlarmModel) = CoroutineScope(IO).launch { alarmDAO.delete(alarmModel) }

    fun getAlarms() = alarmDAO.getAll()
}