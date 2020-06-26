package com.vladmakarenko.weatherutils.utils.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vladmakarenko.weatherutils.models.forecast.CertainForecast

class CertainForecastConverter {
    private val gson = Gson()
    @TypeConverter
    fun toList(data: String?): List<CertainForecast> = if (data == null) {
        emptyList()
    } else {
        val pairType = object : TypeToken<List<CertainForecast>>() {}.type
        gson.fromJson(data, pairType)
    }
    @TypeConverter
    fun fromList(list: List<CertainForecast>) = gson.toJson(list)
}