package com.vladmakarenko.weatherutils.utils.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmPairConverter {

    private val gson = Gson()
    @TypeConverter
    fun toPair(data: String?): Pair<String, Int> = if (data == null) {
        Pair("", 0)
    } else {
        val pairType = object : TypeToken<Pair<String, Int>>() {}.type

        gson.fromJson(data, pairType)
    }
    @TypeConverter
    fun fromPair(icon: Pair<String, Int>) = gson.toJson(icon)

}