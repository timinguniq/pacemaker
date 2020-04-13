package com.devjj.pacemaker.features.pacemaker.historydetail

class OneDaySummary (val sets : Int, val times : Int){
    companion object {
        fun empty() = OneDaySummary(0,0)
    }
}