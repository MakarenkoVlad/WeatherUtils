package com.vladmakarenko.weatherutils.repository.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vladmakarenko.weatherutils.models.forecast.ForecastModel

@Dao
interface ForecastModelDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun set(model: ForecastModel)

    @Query("SELECT * FROM ForecastModel")
    suspend fun get(): ForecastModel?
}