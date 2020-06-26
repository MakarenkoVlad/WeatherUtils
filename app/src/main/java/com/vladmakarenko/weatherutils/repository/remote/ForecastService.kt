package com.vladmakarenko.weatherutils.repository.remote

import com.vladmakarenko.weatherutils.models.forecast.ForecastModel
import com.vladmakarenko.weatherutils.models.currentforecast.CurrentWeather
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {

    @GET("data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("appid") appId: String = API_KEY
    ): Deferred<Response<CurrentWeather>>

    @GET("data/2.5/forecast")
    fun getForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("appid") appId: String = API_KEY
    ): Deferred<Response<ForecastModel>>
}

private const val API_KEY = "8f2d32a837b67aa21ee999709ba986cc"