package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devjj.pacemaker.core.extension.empty
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.home.HomeData

@Entity(tableName = "exercises")
data class ExerciseEntity(@PrimaryKey(autoGenerate = true) var id: Int,
                          var part:Int,
                          var name:String,
                          var mass:Int,
                          var rep:Int,
                          var set:Int,
                          var interval:Int,
                          var achivement: Boolean){

    companion object{
        fun empty() = ExerciseEntity(0, 0, String.empty(), 0, 0, 0, 0, false)
    }

    // ExerciseEntity를 HomeData로 변환하는 함수.
    fun toHomeData() = HomeData(id, part, name, mass, rep, set, interval)

    // ExerciseEntity를 AdditionData로 변환하는 함수.
    fun toAdditionData() = AdditionData(id, part, name, mass, rep, set, interval)

}
