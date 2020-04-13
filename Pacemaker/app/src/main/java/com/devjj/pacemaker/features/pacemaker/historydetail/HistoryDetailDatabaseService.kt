package com.devjj.pacemaker.features.pacemaker.historydetail

import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import javax.inject.Inject

class HistoryDetailDatabaseService
@Inject constructor(val db : ExerciseHistoryDatabase){
    fun historiesByDate(date : String) = db.ExerciseHistoryDAO().getAllByDate(date)
    fun switchAchievement(id : Int) = db.ExerciseHistoryDAO().switchAchievement(id)
    fun updateAchievementRate(date : String) = db.ExerciseHistoryDAO().updateAchievementRate(date)
    fun getOneDaySummary(date : String) = db.ExerciseHistoryDAO().getOneDaySummary(date)
}
