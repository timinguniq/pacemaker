package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.extension.empty

data class ExerciseData(val id: Int,
                        val part_img: Int,
                        val name: String,
                        val mass: Int,
                        val set: Int,
                        val interval: Int,
                        var achivement: Boolean) {

    companion object{
        fun empty() = ExerciseData(0, 0, String.empty(), 0, 0, 0, false)
    }
}