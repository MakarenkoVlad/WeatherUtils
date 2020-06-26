package com.vladmakarenko.weatherutils.models.forecast


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ForecastModel(
    @PrimaryKey
    @SerializedName("key")
    val key: Int,
    @SerializedName("list")
    val list: List<CertainForecast>
){
    constructor() : this(0, listOf())
}