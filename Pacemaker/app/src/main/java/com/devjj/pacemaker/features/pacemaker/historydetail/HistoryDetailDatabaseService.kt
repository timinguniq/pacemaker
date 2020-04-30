package com.devjj.pacemaker.features.pacemaker.historydetail

import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import javax.inject.Inject

class HistoryDetailDatabaseService
@Inject constructor(val db : ExerciseHistoryDatabase){
    fun historiesByDate(date : String) = db.ExerciseHistoryDAO().getAllByDate(date)
    fun getOneDaySets(date : String) = db.ExerciseHistoryDAO().getOneDaySets(date)
}
