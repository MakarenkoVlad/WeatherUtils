package com.vladmakarenko.weatherutils.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmModel(
    @PrimaryKey(autoGenerate = true)
    var key: Int,
    var minutes: Int,
    var hours: Int,
    var title: String,
    var description: String,
    var icon: Pair<String, Int>,
    var isScheduled: Boolean,
    var time: Long
) {
    constructor() : this(
        key = 0,
        minutes = 0,
        hours = 0,
        title = "",
        description = "",
        icon = "01d" to 1,
        isScheduled = false,
        time = -1
    )
}