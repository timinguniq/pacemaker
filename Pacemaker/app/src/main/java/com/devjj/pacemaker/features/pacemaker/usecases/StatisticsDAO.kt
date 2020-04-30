package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.devjj.pacemaker.features.pacemaker.entities.StatisticsEntity

@Dao
interface StatisticsDAO {

    @Query("SELECT * FROM statistics")
    fun getAll() : List<StatisticsEntity>

    @Query("UPDATE statistics SET height = :height and weight = :weight where date = :date")
    fun updateProfile(date : String, height : Float,weight : Float)

    @Insert(onConflict = REPLACE)
    fun insert(vararg statisticsEntity: StatisticsEntity)

    @Delete
    fun delete(statisticsEntity: StatisticsEntity)


}
