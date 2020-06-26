package com.vladmakarenko.weatherutils.repository.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vladmakarenko.weatherutils.models.AlarmModel
import com.vladmakarenko.weatherutils.models.forecast.ForecastModel
import com.vladmakarenko.weatherutils.utils.typeconverter.AlarmPairConverter
import com.vladmakarenko.weatherutils.utils.typeconverter.CertainForecastConverter
import com.vladmakarenko.weatherutils.utils.typeconverter.WeatherConverter

@Database(entities = [ForecastModel::class, AlarmModel::class], version = 3)
@TypeConverters(AlarmPairConverter::class, WeatherConverter::class, CertainForecastConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getForecastDAO(): ForecastModelDAO
    abstract fun getAlarmDAO(): AlarmModelDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        operator fun invoke(context: Context): WeatherDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context,
                        WeatherDatabase::class.java,
                        "weather_db"
                    )
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}