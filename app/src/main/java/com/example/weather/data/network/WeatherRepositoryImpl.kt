package com.example.weather.data.network

import com.example.weather.data.network.pojo.WeatherResponse
import com.example.weather.domain.WeatherRepository
import retrofit2.await

class WeatherRepositoryImpl: WeatherRepository {
    private val weatherApi = RetrofitClient.weatherApi
    override suspend fun getWeatherWithCoord(latitude: Double, longitude: Double): WeatherResponse {
        return weatherApi.getWeather(latitude, longitude).await()
    }
}