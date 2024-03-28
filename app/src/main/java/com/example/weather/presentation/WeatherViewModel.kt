package com.example.weather.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.data.network.WeatherRepositoryImpl
import com.example.weather.data.network.pojo.WeatherResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepositoryImpl()
    private val _weatherInfo: MutableLiveData<WeatherResponse> = MutableLiveData()
    private val _coordinates: MutableLiveData<List<Double>> = MutableLiveData()

    private lateinit var geocoder: Geocoder
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    val weatherInfo: LiveData<WeatherResponse>
        get() = _weatherInfo
    val coordinates: LiveData<List<Double>>
        get() = _coordinates


    suspend fun getInfo(latitude: Double, longitude: Double) {
        val weatherResponse = repository.getWeatherWithCoord(latitude, longitude)
        Log.d("getInfo", weatherResponse.toString())
        _weatherInfo.value = weatherResponse
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context) {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Проверяем наличие разрешений на доступ к местоположению
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("getLastKnownLocation", "No location permissions")
            return
        }

        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null

        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null) {
                Log.d("getLastKnownLocation", "Found last known location: $location")
                break
            }
        }

        if (location == null) {
            Log.e("getLastKnownLocation", "Last known location not available")
            return
        }

        // Ваши дальнейшие действия с полученным местоположением
        latitude = location.latitude
        longitude = location.longitude
        _coordinates.value = listOf(latitude, longitude)
        Log.d("getLastKnownLocation", "Latitude: $latitude, Longitude: $longitude")
    }

    fun getTime(time: Long): String {
        val date =
            Date(time * 1000) // умножаем на 1000, чтобы преобразовать секунды в миллисекунды

        val sdf = SimpleDateFormat("HH:mm")

        val formattedTime: String = sdf.format(date)
        return formattedTime
    }

    fun checkPermissions(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions(context)
        } else {
            getLastKnownLocation(context)
        }
    }

    private fun requestLocationPermissions(context: Context) {
        // Запрашиваем разрешения у пользователя
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1001
        )
    }

    fun getCity(context: Context): String {
        Log.d("Город", latitude.toString())
        geocoder = Geocoder(context, Locale("ru"))
        val adress = geocoder.getFromLocation(latitude, longitude, 1)
        Log.d("Город", adress.toString())
        if (adress!![0].locality != null) {
            return adress[0].locality
        }
        val country = adress[0].countryName.trim()
        val adressLines = adress[0].getAddressLine(0).split(",").map { it.trim() }
        val countryInd = adressLines.indexOf(country)
        Log.d("Город", adressLines.toString())
        if (countryInd > 0) {
            if (adressLines[countryInd - 1] == adress[0].subAdminArea) {
                return adress[0].subAdminArea
            }
            return if (countryInd >= 3) {
                adressLines[countryInd - 2]
            } else {
                adressLines[0]
            }
        } else {
            if (adressLines.size >= 3) {
                return adressLines[adressLines.size - 4]
            }
        }
        return adressLines[0]

    }


}