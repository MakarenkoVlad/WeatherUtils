package com.vladmakarenko.weatherutils.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.utils.formatter.ForecastFormatter
import kotlinx.android.synthetic.main.forecast_subitem.view.*

class ForecastItemAdapter(private val list: List<ForecastFormatter>): RecyclerView.Adapter<ForecastItemViewHolder>() {
    
    private companion object{
        const val TAG = "ForecastAdapter"
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ForecastItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.forecast_subitem, parent, false)
        )

    override fun getItemCount(): Int{
        Log.d(TAG, "getItemCount: ${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int){
        val forecastFormatter = list[position]
        Log.d(TAG, "onBindViewHolder: $forecastFormatter")
        with(holder){
            date.text = forecastFormatter.date
            temperature.text = forecastFormatter.temperature
            description.text = forecastFormatter.description
            condition.text = forecastFormatter.condition
            Picasso.get()
                .load(forecastFormatter.iconURL)
                .into(icon)
        }
    }
}

class ForecastItemViewHolder(view: View): RecyclerView.ViewHolder(view){
    val date = view.forecast_item_date
    val temperature = view.forecast_item_temperature
    val description = view.forecast_item_description
    val condition = view.forecast_item_condition
    val icon = view.forecast_item_icon
}