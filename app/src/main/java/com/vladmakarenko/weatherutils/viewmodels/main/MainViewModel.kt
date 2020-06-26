package com.vladmakarenko.weatherutils.viewmodels.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vladmakarenko.weatherutils.models.AlarmModel
import com.vladmakarenko.weatherutils.models.LocationModel
import com.vladmakarenko.weatherutils.repository.WeatherRepository
import com.vladmakarenko.weatherutils.utils.formatter.CurrentWeatherFormatter
import com.vladmakarenko.weatherutils.utils.formatter.ForecastFormatter
import com.vladmakarenko.weatherutils.utils.split
import java.util.*

class MainViewModel(
    private val liveLocation: LiveData<LocationModel>,
    private val lifecycleOwner: LifecycleOwner,
    private val appContext: Context,
    private val locale: Locale
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
        private const val HOUR = 1000 * 60 * 60L
        const val HOURx3 = HOUR * 3L
        private const val DAY = HOUR * 24L
    }

    private lateinit var repository: WeatherRepository
    private lateinit var alarmModelLiveData: LiveData<AlarmModel>

    private val mWeatherFormatterLiveData = MutableLiveData<CurrentWeatherFormatter>()
    val currentWeatherFormatterLiveData: LiveData<CurrentWeatherFormatter>
        get() = mWeatherFormatterLiveData

    private val mForecastFormatterLiveData = MutableLiveData<List<List<ForecastFormatter>>>()
    val forecastFormatterLiveData: LiveData<List<List<ForecastFormatter>>>
        get() = mForecastFormatterLiveData

    val alarmModels: LiveData<List<AlarmModel>>
        get() = repository.getAlarms()

    init {
        setLocationObserver()
    }

    fun setAlarmModelLiveData(alarmModelLiveData: LiveData<AlarmModel>) {
        this.alarmModelLiveData = alarmModelLiveData
        this.alarmModelLiveData.observe(lifecycleOwner, Observer {
            repository.addAlarm(it)
        })
    }

    fun deleteAlarmModel(alarmModel: AlarmModel) = repository.deleteAlarm(alarmModel)

    private fun setLocationObserver() {
        liveLocation.observe(lifecycleOwner, Observer {
            Log.d(TAG, "setLocationObserver: $it")
            repository = WeatherRepository(locale, appContext)
            repository.location = it
            setWeatherObserver()
            repository.getWeather()
            setForecastObserver()
            repository.getForecast()
        })
    }

    private fun setWeatherObserver() {
        repository.currentWeatherLiveData.observe(lifecycleOwner, Observer {
            mWeatherFormatterLiveData.value =
                CurrentWeatherFormatter(
                    it,
                    appContext
                )
        })
    }

    private fun setForecastObserver() {
        repository.forecastLiveData.observe(lifecycleOwner, Observer { forecastModel ->
            val calendar = Calendar.getInstance(locale)
            val tempList = forecastModel.list
            val tempListItem = tempList[0]
//            tempListItem.dt *= 1000
            calendar.timeInMillis = tempListItem.dt
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val start = (24 - hour) / 3 + 1
//            tempList.forEachIndexed { index, certainForecast ->
//                certainForecast.dt = tempListItem.dt + HOURx3 * index
//            }
            val forecastFormatters = tempList.map {
                ForecastFormatter(
                    it,
                    appContext,
                    locale
                )
            }
            mForecastFormatterLiveData.value = forecastFormatters.split(start)
        })
    }
}