package com.devjj.pacemaker.features.pacemaker.history

data class SumOfSetsAndMass(val sets : Int, val mass : Int){
    companion object {
        fun empty() = SumOfSetsAndMass(0,0)
    }
}