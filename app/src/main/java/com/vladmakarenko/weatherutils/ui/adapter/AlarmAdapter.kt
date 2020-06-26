package com.vladmakarenko.weatherutils.ui.adapter

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.models.AlarmModel
import com.vladmakarenko.weatherutils.workers.AlarmCreatorWorker
import kotlinx.android.synthetic.main.alarm_item.view.*
import java.util.*

class AlarmAdapter(
    alarmsLiveData: LiveData<List<AlarmModel>>,
    lifecycleOwner: LifecycleOwner,
    private val appContext: Context
) :
    RecyclerView.Adapter<AlarmViewHolder>() {
    private var list = listOf<AlarmModel>()
    private val workManager = WorkManager.getInstance(appContext)


    companion object{
        private const val TAG = "AlarmAdapter"
    }

    init {
        alarmsLiveData.observe(lifecycleOwner, Observer {
            val oldList = list
            val diffResult = DiffUtil.calculateDiff(
                AlarmItemDiffCallback(
                    oldList,
                    it
                )
            )
            list = it
            diffResult.dispatchUpdatesTo(this)
            workManager.enqueue(OneTimeWorkRequestBuilder<AlarmCreatorWorker>().build())
        })
    }

    class AlarmItemDiffCallback(
        var oldList: List<AlarmModel>,
        var newList: List<AlarmModel>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].key == newList[newItemPosition].key

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AlarmViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.alarm_item, parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        with(holder) {
            item = list[position]

            if (title.text.isNullOrBlank())
                title.visibility = View.GONE
            else{
                title.text = item.title
                title.visibility = View.VISIBLE
            }

            if (description.text.isNullOrBlank())
                description.visibility = View.GONE
            else{
                description.text = item.description
                description.visibility = View.VISIBLE
            }

            Log.d(TAG, "onBindViewHolder: ${item.icon.first}")
            icon.setImageResource(item.icon.second)
            timeTo.text = String.format(appContext.getString(R.string.make_a_call_for), item.hours, item.minutes)
            if (item.isScheduled) {
                val date = Date(
                    item.time
                )
                state.setImageResource(R.drawable.ic_baseline_check)
                time.visibility = View.VISIBLE
                time.text = String.format(appContext.getString(R.string.gonna_call_in), DateFormat.getMediumDateFormat(appContext).format(date) + " " + DateFormat.getTimeFormat(appContext).format(date))
            } else {
                time.visibility = View.GONE
                state.setImageResource(R.drawable.ic_baseline_close)
            }
        }
    }

}

class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    lateinit var item: AlarmModel
    val title = view.alarm_title!!
    val description = view.alarm_description!!
    val icon = view.alarm_icon!!
    val timeTo = view.alarm_time_to!!
    val time = view.alarm_time!!
    val state = view.alarm_state!!
}