package com.devjj.pacemaker.features.pacemaker.play

import com.devjj.pacemaker.core.extension.empty

data class PlayPopupData(val id: Int,
                         val part_img: Int,
                         val name: String,
                         val mass: Int,
                         val rep: Int,
                         val set: Int,
                         val interval: Int,
                         var achivement: Boolean) {

    companion object{
        fun empty() = PlayPopupData(-1, 0, String.empty(), 0, 0, 0, 0, false)
    }
}