package com.example.weather.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.network.WeatherRepositoryImpl
import com.example.weather.data.network.pojo.WeatherResponse

class WeatherViewModel : ViewModel() {
    val repository = WeatherRepositoryImpl()
    private val _weatherInfo: MutableLiveData<WeatherResponse> = MutableLiveData()
    val weatherInfo: LiveData<WeatherResponse>
        get() = _weatherInfo


    suspend fun getInfo(latitude: Double, longitude: Double) {
        val weatherResponse = repository.getWeatherWithCoord(latitude, longitude)
        _weatherInfo.value = weatherResponse
    }
}