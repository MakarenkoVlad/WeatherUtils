package com.vladmakarenko.weatherutils.models.currentforecast


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("main")
    val main: String
)