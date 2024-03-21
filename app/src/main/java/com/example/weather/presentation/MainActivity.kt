package com.example.weather.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.data.network.RetrofitClient
import com.example.weather.data.network.pojo.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

//        val latitude = 59.0
//        val longitude = 30.0
//        val apiKey = "34ce3d40fac1089ba74a68793377cd42"
//        val call = RetrofitClient.weatherApi.getWeather(latitude, longitude, apiKey)
//        call.enqueue(object : Callback<WeatherResponse> {
//            override fun onResponse(
//                call: Call<WeatherResponse>,
//                response: Response<WeatherResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val weatherResponse = response.body()
//                    Log.d("Погода", weatherResponse?.main?.temp.toString())
//                } else {
//                    // Обработка ошибки
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
//                TODO()
//            }
//        })
    }
}