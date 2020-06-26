package com.vladmakarenko.weatherutils.utils.formatter

import android.content.Context
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.models.currentforecast.CurrentWeather

class CurrentWeatherFormatter(current: CurrentWeather, context: Context) {

    companion object {
        private const val kelvinSubtract = -273.15
    }

    val cloudiness = context.getString(R.string.cloud_cover) + " " + current.clouds.all + " %"
    val coords =
        context.getString(R.string.latitude) + " " + current.coord.lat + context.getString(R.string.longtitude) + current.coord.lon
    val humidity = context.getString(R.string.humidity) + " " + current.main.humidity + " %"
    val temperature =
        context.getString(R.string.temperature) + " " + (current.main.tempMin + kelvinSubtract).toInt() + " - " + (current.main.tempMin + kelvinSubtract).toInt()
    val city = current.name
    val country = current.sys.country
    val sunrisesunset = context.getString(R.string.sunrise) + " " + current.sys.sunrise + " " + context.getString(
        R.string.sunset
    ) + " " + current.sys.sunset
    private val weather = current.weather[0]
    val iconURL = "http://openweathermap.org/img/wn/${weather.icon}@2x.png"
    val description = weather.description
    val condition = weather.main
    val windSpeed = current.wind.speed
    val windDegree = current.wind.deg
}