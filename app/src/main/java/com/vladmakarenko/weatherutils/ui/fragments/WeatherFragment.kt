package com.vladmakarenko.weatherutils.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.ui.activities.MainActivity
import com.vladmakarenko.weatherutils.ui.adapter.ForecastAdapter
import com.vladmakarenko.weatherutils.viewmodels.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.fragment_weather.view.*

class WeatherFragment : Fragment() {

    companion object {
        private const val TAG = "CurrentWeatherFragment"
    }

    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_weather, container, false)

        mainViewModel = (activity as MainActivity).mainViewModel

        fragment.curr_city.setOnClickListener {
            onCityClick()
        }
        setWeatherFormatterObserver()
        setForecastFormatterObserver()
        return fragment
    }
    
    private fun onCityClick(){
        curr_coords.visibility = View.VISIBLE
    }

    private fun setWeatherFormatterObserver(){
        mainViewModel.currentWeatherFormatterLiveData.observe(viewLifecycleOwner, Observer { formattedWeather ->
            if(weather_fragment_container.visibility == View.INVISIBLE)
                weather_fragment_container.visibility = View.VISIBLE

            curr_city.text = formattedWeather.city
            curr_coords.text = formattedWeather.coords
            Picasso.get()
                .load(formattedWeather.iconURL)
                .into(curr_weather_icon)
            curr_condition.text = formattedWeather.condition
            curr_description.text = formattedWeather.description
            curr_temperature.text = formattedWeather.temperature
            curr_cloudiness.text = formattedWeather.cloudiness
            curr_humidity.text = formattedWeather.humidity
        })
    }

    private fun setForecastFormatterObserver(){
        mainViewModel.forecastFormatterLiveData.observe(viewLifecycleOwner, Observer {
            with(forecast_recycler){
                layoutManager = LinearLayoutManager(context)
                adapter =
                    ForecastAdapter(it)
                setHasFixedSize(true)
            }
        })
    }
}