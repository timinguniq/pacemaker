package com.devjj.pacemaker.features.pacemaker.statistics

import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import javax.inject.Inject

class StatisticsDatabaseService
@Inject constructor(val db : StatisticsDatabase){
    fun statistics() = db.StatisticsDAO().getAll()
    fun updateProfile(date : String, height:Float,weight:Float) = db.StatisticsDAO().updateProfile(date, height, weight)
}