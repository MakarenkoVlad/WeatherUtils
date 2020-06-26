package com.vladmakarenko.weatherutils.utils.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vladmakarenko.weatherutils.models.forecast.Weather

class WeatherConverter {
    private val gson = Gson()
    @TypeConverter
    fun toList(data: String?): List<Weather> = if (data == null) {
        emptyList()
    } else {
        val pairType = object : TypeToken<List<Weather>>() {}.type
        gson.fromJson(data, pairType)
    }
    @TypeConverter
    fun fromList(list: List<Weather>) = gson.toJson(list)
}