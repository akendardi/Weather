package com.example.weather.presentation

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.data.network.pojo.WeatherResponse
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale

private const val EXTRA_DAY = "day"
private const val EXTRA_NIGHT = "night"

class Weather : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var geocoder: Geocoder


    private var temp: Double = 0.0
    private var maxTemp: Double = 0.0
    private var minTemp: Double = 0.0
    private var tempFeel: Double = 0.0
    private var sunUp: String = ""
    private var sunDown: String = ""
    private var description: Int = 0
    private var city: String = ""
    private var dayTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.weather)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        geocoder = Geocoder(this, Locale("ru"))
        weatherViewModel.checkPermissions(this)
        weatherViewModel.getLastKnownLocation(this)
        weatherObserve()


    }

    private fun launchFragmentHeader() {
        val fragment = FragmentHeader.newInstance(
            temp,
            minTemp,
            maxTemp,
            tempFeel,
            sunUp,
            sunDown,
            city,
            description,
            dayTime
        )
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_header, fragment)
            .commit()
    }

    private fun weatherObserve() {

        weatherViewModel.coordinates.observe(this) {
            this.latitude = it[0]
            this.longitude = it[1]
            Log.d("Координаты", it.toString())
            lifecycleScope.launch {
                weatherViewModel.getInfo(latitude, longitude)
            }
            weatherViewModel.weatherInfo.observe(this) {
                Log.d("Погода", it.toString())
                Log.d("Температура", temp.toString())
                getArguments(it)
                launchFragmentHeader()
            }
        }
    }


    private fun getArguments(weather: WeatherResponse) {
        city = weatherViewModel.getCity(this)
        temp = weather.main.temp
        minTemp = weather.main.temp_min
        maxTemp = weather.main.temp_max
        tempFeel = weather.main.feels_like
        sunUp = weatherViewModel.getTime(weather.sys.sunrise)
        sunDown = weatherViewModel.getTime(weather.sys.sunset)
        description = weather.weather.last().id
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dayTime()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun dayTime() {
        val hourUp = sunUp.split(":")[0].toInt()
        val hourDown = sunDown.split(":")[0].toInt()
        val minuteUp = sunUp.split(":")[1].toInt()
        val minuteDown = sunDown.split(":")[1].toInt()

        val currentTime = LocalTime.now()
        val currentHour = currentTime.hour
        val currentMinute = currentTime.minute

        dayTime = if (currentHour > hourDown || currentHour < hourUp) {
            EXTRA_NIGHT
        } else if ((currentHour == hourDown) && (currentMinute > minuteDown)) {
            EXTRA_NIGHT
        } else if ((currentHour == hourUp) && (currentMinute < minuteUp)) {
            EXTRA_NIGHT
        } else {
            EXTRA_DAY
        }
        Log.d("Говно", dayTime)
    }


}
