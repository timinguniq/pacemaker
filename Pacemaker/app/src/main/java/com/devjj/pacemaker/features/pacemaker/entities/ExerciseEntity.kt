package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devjj.pacemaker.core.extension.empty
import com.devjj.pacemaker.features.pacemaker.home.ExerciseData

@Entity(tableName = "exercises")
data class ExerciseEntity(@PrimaryKey var id: Int,
                          var part:Int,
                          var name:String,
                          var mass:Int,
                          var set:Int,
                          var interval:Int,
                          var achivement: Boolean){

    companion object{
        fun empty() = ExerciseEntity(0, 0, String.empty(), 0, 0, 0, false)
    }

    fun toExerciseData() = ExerciseData(id, part, name, mass, set, interval, achivement)
}