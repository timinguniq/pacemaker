package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.extension.empty

data class HomeData(val id: Int,
                    val part_img: Int,
                    val name: String,
                    val mass: Int,
                    val rep: Int,
                    val setGoal: Int,
                    val interval: Int) {

    companion object{
        fun empty() = HomeData(-1, 0, String.empty(), 0, 0, 0, 0)
    }
}