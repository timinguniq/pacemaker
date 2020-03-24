package com.devjj.pacemaker.features.pacemaker.playpopup

import com.devjj.pacemaker.core.extension.empty

data class PlayPopupData(val id: Int,
                         val part_img: Int,
                         val name: String,
                         val mass: Int,
                         val rep: Int,
                         val setGoal: Int,
                         var setDone: Int,
                         val interval: Int) {

    companion object{
        fun empty() = PlayPopupData(-1, 0, String.empty(), 0, 0, 0, 0, 0)
    }
}