package com.devjj.pacemaker.features.pacemaker.history

import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryDatabaseService
@Inject constructor(val db : ExerciseHistoryDatabase){
    fun histories() = db.ExerciseHistoryDAO().getAllDates()
    fun summary() = db.ExerciseHistoryDAO().getSummary()
    fun getOneMonthSummary(date :String) = db.ExerciseHistoryDAO().getOneMonthSummary(date)
}
