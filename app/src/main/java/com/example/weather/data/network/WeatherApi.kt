package com.example.weather.data.network

import com.example.weather.data.network.pojo.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = "34ce3d40fac1089ba74a68793377cd42",
        @Query("lang") language: String = "ru",
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse> // WeatherResponse - ваш POJO объект для десериализации ответа
}