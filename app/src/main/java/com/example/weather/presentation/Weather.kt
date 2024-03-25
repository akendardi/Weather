package com.example.weather.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
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
    private var permissionToastShown = false

    private var temp: Double = 0.0
    private var description: String = ""
    private var city: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.weather)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        geocoder = Geocoder(this, Locale.getDefault())
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Если разрешения не предоставлены, запрашиваем их у пользователя
            requestLocationPermissions()
        } else {
            // Разрешения уже предоставлены, можно выполнять операции с местоположением
            getLastKnownLocation(this)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            // Проверяем результат запроса разрешений
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Разрешения были предоставлены, можно выполнять операции с местоположением
                getLastKnownLocation(this)
            } else {
                // Разрешения не были предоставлены, сообщаем об этом пользователю
                if (!permissionToastShown) {
                    Toast.makeText(this, "Для работы приложения необходимо разрешение на доступ к местоположению.", Toast.LENGTH_LONG).show()
                    permissionToastShown = true // Устанавливаем флаг, чтобы сообщение показывалось только один раз
                }
                // Запрашиваем разрешения у пользователя повторно
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        // Запрашиваем разрешения у пользователя
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            1001)
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
            Log.d("Температура", temp.toString())
            getArguments(it)
            launchFragmentHeader(temp, city, description)

        }
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context) {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Проверяем наличие разрешений на доступ к местоположению
        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Log.e("getLastKnownLocation", "No location permissions")
            return
        }

        // Получаем список доступных провайдеров местоположения
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null

        // Ищем последнее известное местоположение среди всех провайдеров
        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null) {
                Log.d("getLastKnownLocation", "Found last known location: $location")
                break
            }
        }

        // Проверяем, удалось ли получить последнее известное местоположение
        if (location == null) {
            Log.e("getLastKnownLocation", "Last known location not available")
            return
        }

        // Ваши дальнейшие действия с полученным местоположением
        val latitude = location.latitude
        val longitude = location.longitude
        Log.d("getLastKnownLocation", "Latitude: $latitude, Longitude: $longitude")
        weatherObserve(latitude, longitude)

    }



    fun getArguments(weather: WeatherResponse){
        //TODO city = getCityFromLocation(latitude, longitude, geocoder)
        temp = weather.main.temp
        description = weather.weather.last().description
    }




}
