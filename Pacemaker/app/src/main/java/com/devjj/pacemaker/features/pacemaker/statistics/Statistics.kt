package com.devjj.pacemaker.features.pacemaker.statistics

import com.devjj.pacemaker.core.extension.empty

data class Statistics(val id : Int , val date : String , val height : Float , val weight : Float){
    companion object {
        fun empty() = Statistics(0, String.empty(), 0f, 0f)
    }

}