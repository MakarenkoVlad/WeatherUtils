package com.vladmakarenko.weatherutils.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.repository.local.WeatherIconsProvider
import kotlinx.android.synthetic.main.weather_spinner_item.view.*

class WeatherIconsAdapter(context: Context) : ArrayAdapter<Pair<String, Int>>(context, 0) {
    private var dayOrNight = true
    var icons = WeatherIconsProvider.getIcons(dayOrNight)
        private set

    override fun getCount() = icons.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    fun changeList() {
        dayOrNight = !dayOrNight
        icons = WeatherIconsProvider.getIcons(dayOrNight)
        notifyDataSetChanged()
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView == null) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.weather_spinner_item, parent, false)
                .apply {
                    spinner_image_view.setImageResource(icons[position].second)
                }
        } else {
            convertView.spinner_image_view.setImageResource(icons[position].second)
            convertView
        }
    }
}