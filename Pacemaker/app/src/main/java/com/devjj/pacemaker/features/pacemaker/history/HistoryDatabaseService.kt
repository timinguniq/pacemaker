package com.devjj.pacemaker.features.pacemaker.history

import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryDatabaseService
@Inject constructor(val db : ExerciseHistoryDatabase){
    //@Inject lateinit var db : ExerciseHistoryDatabase
    fun histories() = db.ExerciseHistoryDAO().getAll()
}
