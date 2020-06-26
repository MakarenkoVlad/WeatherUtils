package com.vladmakarenko.weatherutils.models.forecast


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)