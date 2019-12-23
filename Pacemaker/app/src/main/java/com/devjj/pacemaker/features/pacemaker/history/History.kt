package com.devjj.pacemaker.features.pacemaker.history

import com.devjj.pacemaker.core.extension.empty

data class History(val id: Int, val date: String,  val achievementRate : Int) {
    companion object{
        fun empty() = History(0,String.empty(),0)
    }
}