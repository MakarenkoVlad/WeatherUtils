package com.vladmakarenko.weatherutils.models.currentforecast


import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
)