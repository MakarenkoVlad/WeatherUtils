package com.vladmakarenko.weatherutils.repository.local

import com.vladmakarenko.weatherutils.R

object WeatherIconsProvider {
    fun getIcons(state: Boolean)= if (state){
        dayIcons
    } else {
        nightIcons
    }

    private val dayIcons = listOf(
        "01d" to R.drawable.w01d2x,
        "02d" to R.drawable.w02d2x,
        "03d" to R.drawable.w03d2x,
        "04d" to R.drawable.w04d2x,
        "09d" to R.drawable.w09d2x,
        "10d" to R.drawable.w10d2x,
        "11d" to R.drawable.w11d2x,
        "13d" to R.drawable.w13d2x,
        "50d" to R.drawable.w50d2x
    )
    private val nightIcons = listOf(
        "01n" to R.drawable.w01n2x,
        "02n" to R.drawable.w02n2x,
        "03n" to R.drawable.w03n2x,
        "04n" to R.drawable.w04n2x,
        "09n" to R.drawable.w09n2x,
        "10n" to R.drawable.w10n2x,
        "11n" to R.drawable.w11n2x,
        "13n" to R.drawable.w13n2x,
        "50n" to R.drawable.w50n2x
    )
}