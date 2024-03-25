package com.example.weather.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.data.network.pojo.Weather
import com.example.weather.data.network.pojo.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.Locale


class Weather : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var geocoder: Geocoder

    private var temp: Double = 0.0
    private var description: String = ""
    private var city: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.weather)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        geocoder = Geocoder(this, Locale.getDefault())
        getLastKnownLocation(this)

    }

    private fun launchFragmentHeader(temp: Double, city: String, description: String) {
        val fragment = FragmentHeader.newInstance(temp, city, description)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_header, fragment)
            .commit()
    }

    private fun weatherObserve(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            weatherViewModel.getInfo(latitude, longitude)
        }
        weatherViewModel.weatherInfo.observe(this) {
            Log.d("Погода", it.toString())
            Log.d("Город", city)
            getArguments(it)
            launchFragmentHeader(temp, city, description)

        }
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context) {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null
        for (i in providers.size - 1 downTo 0) {
            location= locationManager.getLastKnownLocation(providers[i])
            if (location != null)
                break
        }
        val gps = DoubleArray(2)
        if (location != null) {
            gps[0] = location.getLatitude()
            gps[1] = location.getLongitude()
            latitude = gps[0]
            longitude = gps[1]
            Log.e("gpsLat",gps[0].toString())
            Log.e("gpsLong",gps[1].toString())
            weatherObserve(latitude, longitude)
        }
    }



    fun getArguments(weather: WeatherResponse){
        //TODO city = getCityFromLocation(latitude, longitude, geocoder)
        temp = weather.main.temp
        description = weather.weather.last().description
    }




}
