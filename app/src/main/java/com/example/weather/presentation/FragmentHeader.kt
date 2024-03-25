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
private const val EXTRA_CITY = "city"
private const val EXTRA_DESCRIPTION = "description"




class FragmentHeader : Fragment() {
    private var temp: Double? = null
    private var city: String? = null
    private var description: String? = null

    private lateinit var tv_city: TextView
    private lateinit var tv_temp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            temp = it.getDouble(EXTRA_TEMP)
            city = it.getString(EXTRA_CITY)
            description = it.getString(EXTRA_DESCRIPTION)
            Log.d("Температура onCreate", temp.toString())
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
    }
    private fun setupData(){
        tv_city.text = city
        tv_temp.text = temp?.toInt().toString() + "°C"
    }

    companion object {
        @JvmStatic
        fun newInstance(temp: Double, city: String, description: String) =
            FragmentHeader().apply {
                arguments = Bundle().apply {
                    putDouble(EXTRA_TEMP, temp)
                    putString(EXTRA_CITY, city)
                    putString(EXTRA_DESCRIPTION, description)
                }
            }
    }
}