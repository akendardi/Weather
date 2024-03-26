package com.example.weather.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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




class FragmentHeader : Fragment() {
    private var temp: Double? = null
    private var minTemp: Double? = null
    private var maxTemp: Double? = null
    private var tempFeel: Double? = null
    private var sunUp: String? = null
    private var sunDown: String? = null
    private var city: String? = null
    private var description: String? = null
    private var dayTime: String? = null

    private lateinit var tv_city: TextView
    private lateinit var tv_temp: TextView
    private lateinit var tv_sunUp: TextView
    private lateinit var tv_sunDown: TextView

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
            description = it.getString(EXTRA_DESCRIPTION)
            description = it.getString(EXTRA_DAYTIME)
            Log.d("arguments", arguments.toString())
        }
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

    private fun initViews(){
        tv_city = view?.findViewById(R.id.textViewTemp)!!
        tv_temp = view?.findViewById(R.id.textViewCity)!!
        tv_sunUp = view?.findViewById(R.id.tv_sunUp)!!
        tv_sunDown = view?.findViewById(R.id.tv_sunDown)!!
    }
    private fun setupData(){
        tv_city.text = city
        tv_temp.text = temp?.toInt().toString() + "°C"
        tv_sunUp.text = sunUp
        tv_sunDown.text = sunDown
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
            description: String,
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
                    putString(EXTRA_DESCRIPTION, description)
                    putString(EXTRA_DAYTIME, dayTime)
                }
            }
    }
}