package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import javax.inject.Inject

class HomeDatabaseService
@Inject constructor(val db: ExerciseDatabase){
    fun home() = db.ExerciseDAO().getAll()
}