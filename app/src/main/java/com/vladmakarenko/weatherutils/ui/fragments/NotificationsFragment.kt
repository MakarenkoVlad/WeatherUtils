package com.vladmakarenko.weatherutils.ui.fragments

import android.content.ClipData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.ui.activities.MainActivity
import com.vladmakarenko.weatherutils.ui.adapter.AlarmAdapter
import com.vladmakarenko.weatherutils.ui.adapter.AlarmViewHolder
import com.vladmakarenko.weatherutils.ui.alertdialog.ScheduleDialogFragment
import com.vladmakarenko.weatherutils.viewmodels.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var workManager: WorkManager

    private val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = (viewHolder as AlarmViewHolder).item
            workManager.cancelAllWorkByTag(item.key.toString())
            mainViewModel.deleteAlarmModel(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false)

        mainViewModel = (activity as MainActivity).mainViewModel

        workManager = WorkManager.getInstance(requireContext().applicationContext)

        val scheduleDialog =
            ScheduleDialogFragment()
        mainViewModel.setAlarmModelLiveData(scheduleDialog.alarmModelLiveData)

        //Init fragment view child views
        with(fragmentView) {
            add_notification.setOnClickListener {
                scheduleDialog
                    .show(requireActivity().supportFragmentManager, "schedule")
            }
            with(alarm_recycler){
                layoutManager = LinearLayoutManager(context)
                adapter =
                    AlarmAdapter(
                        mainViewModel.alarmModels,
                        viewLifecycleOwner,
                        fragmentView.context.applicationContext
                    )
                ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
            }
        }

        return fragmentView
    }
}
