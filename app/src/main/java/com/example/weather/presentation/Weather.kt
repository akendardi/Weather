package com.example.weather.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.data.network.pojo.WeatherResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

private const val EXTRA_DAY = "day"
private const val EXTRA_NIGHT = "night"

class Weather : AppCompatActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var geocoder: Geocoder
    private var permissionToastShown = false


    private var temp: Double = 0.0
    private var maxTemp: Double = 0.0
    private var minTemp: Double = 0.0
    private var tempFeel: Double = 0.0
    private var sunUp: String = ""
    private var sunDown: String = ""
    private var description: String = ""
    private var city: String = ""
    private var dayTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.weather)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        geocoder = Geocoder(this, Locale("ru"))
        checkPermissions()

    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
        } else {
            getLastKnownLocation(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            // Проверяем результат запроса разрешений
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Разрешения были предоставлены, можно выполнять операции с местоположением
                getLastKnownLocation(this)
            } else {
                // Разрешения не были предоставлены, сообщаем об этом пользователю
                if (!permissionToastShown) {
                    Toast.makeText(
                        this,
                        "Для работы приложения необходимо разрешение на доступ к местоположению.",
                        Toast.LENGTH_LONG
                    ).show()
                    permissionToastShown =
                        true // Устанавливаем флаг, чтобы сообщение показывалось только один раз
                }
                // Запрашиваем разрешения у пользователя повторно
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        // Запрашиваем разрешения у пользователя
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1001
        )
    }


    private fun launchFragmentHeader(temp: Double, city: String, description: String) {
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

    private fun weatherObserve(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            weatherViewModel.getInfo(latitude, longitude)
        }
        weatherViewModel.weatherInfo.observe(this) {
            Log.d("Погода", it.toString())
            Log.d("Температура", temp.toString())
            getArguments(it)
            launchFragmentHeader(temp, city, description)
        }
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
        Log.d("getLastKnownLocation", "Latitude: $latitude, Longitude: $longitude")
        weatherObserve(latitude, longitude)
    }


    private fun getArguments(weather: WeatherResponse) {
        city = getCity()
        temp = weather.main.temp
        minTemp = weather.main.temp_min
        maxTemp = weather.main.temp_max
        tempFeel = weather.main.feels_like
        sunUp = getTime(weather.sys.sunrise)
        sunDown = getTime(weather.sys.sunset)
        description = weather.weather.last().description
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dayTime()
        }

    }

    private fun getCity(): String {
        Log.d("Город", latitude.toString())
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
        }
        return "Unknown city"

    }

    fun getTime(time: Long): String {
        val date =
            Date(time * 1000) // умножаем на 1000, чтобы преобразовать секунды в миллисекунды

        val sdf = SimpleDateFormat("HH:mm")

        val formattedTime: String = sdf.format(date)
        return formattedTime
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
