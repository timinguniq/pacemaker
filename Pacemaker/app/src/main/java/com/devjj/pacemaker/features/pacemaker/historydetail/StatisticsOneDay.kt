package com.devjj.pacemaker.features.pacemaker.historydetail

import com.devjj.pacemaker.core.extension.empty

class StatisticsOneDay (var id: Int,
                        val date: String,
                        val totalTime: Int,
                        val achievementRate: Int){
    companion object{
        fun empty() = StatisticsOneDay(0,String.empty(),0,0)
    }
}