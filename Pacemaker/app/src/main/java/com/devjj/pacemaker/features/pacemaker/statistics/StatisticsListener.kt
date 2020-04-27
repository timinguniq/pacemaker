package com.devjj.pacemaker.features.pacemaker.statistics

import android.app.Activity
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsListener (val activity: Activity, val setting : SettingSharedPreferences,val statisticsViewModel :StatisticsViewModel){
    fun initListener(){
        activity.fStatistics_swc_mode_bmi.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true ->{
                    setting.isShowbmi = true
                }
                false ->{
                    setting.isShowbmi = false
                }
            }

            statisticsViewModel.loadStatistics()

        }
    }
}