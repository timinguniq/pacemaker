package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.PlayViewSharedPreferences
import javax.inject.Inject

class HomeDatabaseService
@Inject constructor(val db: ExerciseDatabase, val playView: PlayViewSharedPreferences){
    fun home() = db.ExerciseDAO().getAll()
    fun getPlayView() = playView.btnClicked
    fun setPlayView(isClicked: Boolean){
        playView.btnClicked = isClicked
    }
}