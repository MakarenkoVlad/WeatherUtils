package com.vladmakarenko.weatherutils.models.forecast


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class CertainForecast(
    @SerializedName("dt")
    var dt: Long,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("main")
    val main: Main,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
)