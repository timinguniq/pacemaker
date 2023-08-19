package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devjj.pacemaker.core.extension.empty
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData

// division는 분할, part 는 부위 이미지, name은 운동 이름, mass은 운동 무게
// rep는 횟수, setGoal 목표 세트, setDone은 실제로 한 세트수, interval은 하나의 운동에서 쉬는 시간
@Entity(tableName = "exercises")
data class ExerciseEntity(@PrimaryKey(autoGenerate = true) var id: Int,
                          var division:Int,
                          var part:Int,
                          var name:String,
                          var mass:Int,
                          var rep:Int,
                          var setGoal:Int,
                          var setDone: Int,
                          var interval:Int,
                          val achievement: Boolean){

    companion object{
        fun empty() = ExerciseEntity(0, -1, 0, String.empty(), 0, 0, 0, 0, 0, false)
    }

    // ExerciseEntity를 HomeData로 변환하는 함수.
    fun toHomeData() = HomeData(id, part, name, mass, rep, setGoal, interval)

    // ExerciseEntity를 AdditionData로 변환하는 함수.
    fun toAdditionData() = AdditionData(id, part, name, mass, rep, setGoal, interval)

    // ExerciseEntity를 playPopupData로 변환하는 함수.
    fun toPlayPopup() = PlayPopupData(id, part, name, mass, rep, setGoal, setDone, interval, achievement)

}
