package com.example.weather.domain

import com.example.weather.data.network.pojo.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherWithCoord(latitude: Double, longitude: Double): WeatherResponse
}