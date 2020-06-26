package com.vladmakarenko.weatherutils.models.currentforecast


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)