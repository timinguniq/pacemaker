package com.devjj.pacemaker.features.pacemaker.addition

import com.devjj.pacemaker.core.extension.empty

data class AdditionData(val id: Int,
                    val part_img: Int,
                    val name: String,
                    val mass: Int,
                    val rep: Int,
                    val setGoal: Int,
                    val interval: Int) {

    companion object{
        fun empty() = AdditionData(-1, 0, String.empty(), 0, 0, 0, 0)
    }
}