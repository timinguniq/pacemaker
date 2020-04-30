package com.devjj.pacemaker.features.pacemaker.history

data class TotalTimes(val totalTime: Int, val totalTimeOneMonth: Int){
    companion object {
        fun empty() = TotalTimes(0,0)
    }
}