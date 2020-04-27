package com.devjj.pacemaker.features.pacemaker.dialog

import com.devjj.pacemaker.core.extension.empty

data class Profile(val date : String , val height : Float , val weight : Float){
    companion object {
        fun empty() = Profile(String.empty(),0f,0f)
    }
}