package com.vladmakarenko.weatherutils.utils


fun <T> List<T>.split(start: Int, range: Int = 8): List<List<T>> {
    val mutableList = mutableListOf<List<T>>()
    var mStart = start
    if (mStart > 0)
        mutableList.add(subList(0, mStart))
    else
        mStart = 0
    val tempSize = size - mStart
    mutableList.addAll((0 until (tempSize / range)).map { y ->
        (0 until (range)).map { x ->
            this[y * range + x + mStart]
        }
    })
    if (mStart > 0)
        mutableList.add(subList(size - mStart, size))
    return mutableList
}