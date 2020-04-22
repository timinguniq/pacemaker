package com.devjj.pacemaker.core.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devjj.pacemaker.features.pacemaker.entities.StatisticsEntity
import com.devjj.pacemaker.features.pacemaker.usecases.StatisticsDAO

@Database(entities = [StatisticsEntity::class], version = 1, exportSchema = false)
abstract class StatisticsDatabase : RoomDatabase(){
    abstract fun StatisticsDAO() : StatisticsDAO
}