package com.vladmakarenko.weatherutils.ui.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.utils.formatter.ForecastFormatter
import kotlinx.android.synthetic.main.forecast_item.view.*

class ForecastAdapter(private val list: List<List<ForecastFormatter>>) :
    RecyclerView.Adapter<ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ForecastViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.forecast_item, parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val listItem = list[position]
        with(holder) {
            daydate.text = listItem[0].mainDate
            with(recycler) {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                adapter =
                    ForecastItemAdapter(
                        listItem
                    )
            }
            itemView.setOnClickListener {
                with(recycler) {
                    if (isShown) {
                        animate()
                            .alpha(0f)
                            .translationY(0f)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    super.onAnimationEnd(animation)
                                    visibility = View.GONE
                                }
                            })
                            .duration = resources.getInteger(R.integer.short_time_anim).toLong()

                    } else {
                        alpha = 0f
                        translationY = 0f
                        visibility = View.VISIBLE
                        animate()
                            .translationY(1f)
                            .alpha(1f)
                            .setListener(null)
                            .duration = resources.getInteger(R.integer.short_time_anim).toLong()
                    }
                }
            }
        }
    }

}

class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val daydate = view.forecast_item_daydate
    val recycler = view.forecast_item_recycler
}