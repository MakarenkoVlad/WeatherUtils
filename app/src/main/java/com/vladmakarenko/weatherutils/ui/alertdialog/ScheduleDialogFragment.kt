package com.vladmakarenko.weatherutils.ui.alertdialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vladmakarenko.weatherutils.R
import com.vladmakarenko.weatherutils.ui.adapter.WeatherIconsAdapter
import com.vladmakarenko.weatherutils.models.AlarmModel
import kotlinx.android.synthetic.main.schedule_weather_alert_dialog.view.*

class ScheduleDialogFragment : DialogFragment() {
    private val mAlarmModelLiveData = MutableLiveData<AlarmModel>()

    // Connect to view model
    val alarmModelLiveData: LiveData<AlarmModel>
        get() = mAlarmModelLiveData

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alarmModel = AlarmModel()
        val builder = MaterialAlertDialogBuilder(activity)
        val dialogFragment = layoutInflater.inflate(R.layout.schedule_weather_alert_dialog, null)
        val spinnerAdapter =
            WeatherIconsAdapter(
                requireContext()
            )

        with(dialogFragment.schedule_spinner) {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    alarmModel.icon = spinnerAdapter.icons[position]
                }

            }
        }

        with(dialogFragment.schedule_switch) {
            setOnClickListener {
                spinnerAdapter.changeList()
                alarmModel.icon = spinnerAdapter.icons[0]
            }
        }



        dialogFragment.schedule_time_picker.setOnClickListener {
            TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    dialogFragment.schedule_notify_for.text =
                        resources.getString(R.string.notify_for) + " " + String.format(
                            "%02d:%02d",
                            hourOfDay,
                            minute
                        )
                    alarmModel.hours = hourOfDay
                    alarmModel.minutes = minute
                },
                0,
                0,
                true
            )
                .show()
        }

        builder.setView(dialogFragment)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                alarmModel.title = dialogFragment.schedule_title.text.toString()
                alarmModel.description = dialogFragment.schedule_description.text.toString()
                mAlarmModelLiveData.value = alarmModel
            }

        return builder.create()
    }
}
