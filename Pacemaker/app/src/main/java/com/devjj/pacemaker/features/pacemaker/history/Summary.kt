package com.devjj.pacemaker.features.pacemaker.history

data class Summary(val sets : Int, val times : Int, val kgs : Int){
    companion object {
        fun empty() = Summary(0,0,0)
    }
}