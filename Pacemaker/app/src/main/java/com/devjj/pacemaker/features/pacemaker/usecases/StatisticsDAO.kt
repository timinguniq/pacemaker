package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.devjj.pacemaker.features.pacemaker.entities.StatisticsEntity
import com.devjj.pacemaker.features.pacemaker.history.TotalTimes

@Dao
interface StatisticsDAO {

    @Query("SELECT * FROM statistics")
    fun getAll() : List<StatisticsEntity>

    @Query("UPDATE statistics SET height = :height , weight = :weight WHERE date = :date")
    fun updateProfile(date : String, height : Float,weight : Float)

    @Insert(onConflict = REPLACE)
    fun insert(vararg statisticsEntity: StatisticsEntity)

    @Delete
    fun delete(statisticsEntity: StatisticsEntity)

    @Query("SELECT * FROM statistics WHERE date = :date")
    fun getStatisticsOneDay(date: String) : StatisticsEntity

    @Query("SELECT SUM(totalTime) as totalTime, (SELECT SUM(totalTime) FROM statistics WHERE substr(date,0,8) = substr(:date,0,8)) as totalTimeOneMonth FROM statistics")
    fun getTotalTimes(date: String) : TotalTimes

}
