package com.example.weather.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weather.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val EXTRA_TEMP = "temp"
private const val EXTRA_MIN_TEMP = "minTemp"
private const val EXTRA_MAX_TEMP = "maxTemp"
private const val EXTRA_TEMP_FEEL = "tempFeel"
private const val EXTRA_SUN_UP = "sunUp"
private const val EXTRA_SUN_DOWN = "sunDown"
private const val EXTRA_CITY = "city"
private const val EXTRA_DESCRIPTION = "description"

private const val EXTRA_DAY = "day"
private const val EXTRA_NIGHT = "night"
private const val EXTRA_DAYTIME = "dayTime"

private val DAY_RAIN = R.drawable.rain_day





class FragmentHeader : Fragment() {
    private var temp: Double? = null
    private var minTemp: Double? = null
    private var maxTemp: Double? = null
    private var tempFeel: Double? = null
    private var sunUp: String? = null
    private var sunDown: String? = null
    private var city: String? = null
    private var description: Int? = null
    private var dayTime: String? = null

    private lateinit var tv_city: TextView
    private lateinit var tv_temp: TextView
    private lateinit var tv_sunUp: TextView
    private lateinit var tv_sunDown: TextView
    private lateinit var iv_descriprion: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            temp = it.getDouble(EXTRA_TEMP)
            maxTemp = it.getDouble(EXTRA_MAX_TEMP)
            minTemp = it.getDouble(EXTRA_MIN_TEMP)
            tempFeel = it.getDouble(EXTRA_TEMP_FEEL)
            sunUp = it.getString(EXTRA_SUN_UP)
            sunDown = it.getString(EXTRA_SUN_DOWN)
            city = it.getString(EXTRA_CITY)
            description = it.getInt(EXTRA_DESCRIPTION)
            dayTime = it.getString(EXTRA_DAYTIME)
            Log.d("arguments", arguments.toString())
        }
        Log.d("Фрагмент", arguments.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupData()
    }

    private fun initViews() {
        tv_city = view?.findViewById(R.id.textViewTemp)!!
        tv_temp = view?.findViewById(R.id.textViewCity)!!
        tv_sunUp = view?.findViewById(R.id.tv_sunUp)!!
        tv_sunDown = view?.findViewById(R.id.tv_sunDown)!!
        iv_descriprion = view?.findViewById(R.id.imageViewDescription)!!
    }

    private fun setupData() {
        tv_city.text = city
        tv_temp.text = temp?.toInt().toString() + "°C"
        tv_sunUp.text = sunUp
        tv_sunDown.text = sunDown
    }

    fun getWeatherDescription(weatherCode: Int): String {
        return when (weatherCode) {
            200 -> "Гроза с небольшим дождем"
            201 -> "Гроза с дождем"
            202 -> "Гроза с проливным дождем"
            210 -> "Небольшая гроза"
            211 -> "Гроза"
            212 -> "Сильная гроза"
            221 -> "Переменная гроза"
            230 -> "Гроза с легким моросящим дождем"
            231 -> "Гроза с моросящим дождем"
            232 -> "Гроза с проливным моросящим дождем"
            300 -> "Легкий моросящий дождь"
            301 -> "Моросящий дождь"
            302 -> "Сильный моросящий дождь"
            310 -> "Легкий дождь"
            311 -> "Дождь"
            312 -> "Сильный дождь"
            313 -> "Ливень"
            314 -> "Сильный ливень"
            321 -> "Переменный дождь"
            500 -> "Легкий дождь"
            501 -> "Умеренный дождь"
            502 -> "Интенсивный дождь"
            503 -> "Очень сильный дождь"
            504 -> "Ливень"
            511 -> "Ледяной дождь"
            520 -> "Легкий проливной дождь"
            521 -> "Проливной дождь"
            522 -> "Сильный проливной дождь"
            531 -> "Переменный проливной дождь"
            600 -> "Легкий снег"
            601 -> "Снег"
            602 -> "Сильный снегопад"
            611 -> "Дождь со снегом"
            612 -> "Легкий дождь и снег"
            613 -> "Дождь и снег"
            615 -> "Легкий дождь и снег"
            616 -> "Дождь и снег"
            620 -> "Легкий снегопад"
            621 -> "Снегопад"
            622 -> "Сильный снегопад"
            701 -> "Туман"
            711 -> "Дымка"
            721 -> "Мгла"
            731 -> "Песчаная буря"
            741 -> "Туман"
            751 -> "Песчаная буря"
            761 -> "Пыльная буря"
            762 -> "Вулканический пепел"
            771 -> "Шквалы"
            781 -> "Торнадо"
            800 -> "Ясно"
            801 -> "Небольшая облачность"
            802 -> "Рассеянные облака"
            803 -> "Облачно с прояснениями"
            804 -> "Пасмурно"
            else -> "Нет данных"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            temp: Double,
            minTemp: Double,
            maxTemp: Double,
            tempFeel: Double,
            sunUp: String,
            sunDown: String,
            city: String,
            description: Int,
            dayTime: String
        ) =
            FragmentHeader().apply {
                arguments = Bundle().apply {
                    putDouble(EXTRA_TEMP, temp)
                    putDouble(EXTRA_MIN_TEMP, minTemp)
                    putDouble(EXTRA_MAX_TEMP, maxTemp)
                    putDouble(EXTRA_TEMP_FEEL, tempFeel)
                    putString(EXTRA_SUN_UP, sunUp)
                    putString(EXTRA_SUN_DOWN, sunDown)
                    putString(EXTRA_CITY, city)
                    putInt(EXTRA_DESCRIPTION, description)
                    putString(EXTRA_DAYTIME, dayTime)
                }
            }
    }
}