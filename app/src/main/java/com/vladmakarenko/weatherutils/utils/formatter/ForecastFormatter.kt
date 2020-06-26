package com.vladmakarenko.weatherutils.utils.formatter

import android.content.Context
import android.text.format.DateFormat
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.models.forecast.CertainForecast
import java.text.SimpleDateFormat
import java.util.*

class ForecastFormatter(certainForecast: CertainForecast, context: Context, private val locale: Locale) {
    private companion object {
        const val kelvinSubtract = -273.15
    }

    val humidity = context.getString(R.string.humidity) + " " + certainForecast.main.humidity + " %"
    val temperature =
        context.getString(R.string.temperature) + " " + (certainForecast.main.tempMin + kelvinSubtract).toInt() + " - " + (certainForecast.main.tempMin + kelvinSubtract).toInt()

    val windSpeed = certainForecast.wind.speed
    val windDegree = certainForecast.wind.deg

    private val weather = certainForecast.weather[0]

    val iconURL = "http://openweathermap.org/img/wn/${weather.icon}@2x.png"
    val condition = weather.main
    val description = weather.description
    val date = DateFormat.getTimeFormat(context).format(Date(certainForecast.dt))
    val mainDate = DateFormat.getLongDateFormat(context).format(Date(certainForecast.dt))
}