package com.vladmakarenko.weatherutils.repository.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vladmakarenko.weatherutils.models.AlarmModel

@Dao
interface AlarmModelDAO {

    @Query("SELECT * FROM alarmmodel")
    fun getAll(): LiveData<List<AlarmModel>>

    @Query("SELECT * FROM alarmmodel")
    suspend fun getAllAsync(): List<AlarmModel>

    @Delete
    fun delete(model: AlarmModel)

    @Query("DELETE FROM alarmmodel WHERE `key` = :key")
    suspend fun deleteByKey(key: Int)

    @Update
    suspend fun update(model: AlarmModel)

    @Insert
    fun insert(model: AlarmModel)

}