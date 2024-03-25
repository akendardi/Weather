package com.example.weather.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.data.network.WeatherRepositoryImpl
import com.example.weather.data.network.pojo.WeatherResponse

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepositoryImpl()
    private val _weatherInfo: MutableLiveData<WeatherResponse> = MutableLiveData()
    val weatherInfo: LiveData<WeatherResponse>
        get() = _weatherInfo


    suspend fun getInfo(latitude: Double, longitude: Double) {
        val weatherResponse = repository.getWeatherWithCoord(latitude, longitude)
        Log.d("getInfo", weatherResponse.toString())
        _weatherInfo.value = weatherResponse
    }


}
