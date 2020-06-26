package com.vladmakarenko.weatherutils.repository.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitImpl {
    private const val BASE_URL = "https://api.openweathermap.org/"
    private val gsonConverterFactory = GsonConverterFactory.create()

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val callAdapter = CoroutineCallAdapterFactory()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(callAdapter)
            .client(okHttpClient)
            .build()
    }

    val forecastService by lazy {
        retrofit.create(ForecastService::class.java)
    }
}