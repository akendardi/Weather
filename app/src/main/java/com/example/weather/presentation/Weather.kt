package com.example.weather.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import kotlinx.coroutines.launch

class Weather : AppCompatActivity() {
    lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        lifecycleScope.launch {
            weatherViewModel.getInfo(50.0, 50.0)
        }
        weatherViewModel.weatherInfo.observe(this){
            Log.d("Погода", it.toString())
        }
    }
}