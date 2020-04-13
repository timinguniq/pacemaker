package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devjj.pacemaker.features.pacemaker.history.History
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetail

@Entity(tableName = "exerciseHistories")
data class ExerciseHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: String,
    val part: Int,
    val name: String,
    val mass: Int,
    val rep: Int,
    val setGoal: Int,
    val setDone: Int,
    val interval: Int,
    val achievement: Boolean,
    val achievementRate: Int,
    val height : Float,
    val weight : Float,
    val totalTime: Int
){
    fun toHistory() = History(id, date, achievementRate)
    fun toHistoryDetail() = HistoryDetail(id,date,part,name,mass,rep,setDone,interval, (if(achievement)1 else 0) ,achievementRate)
    fun isAcheived() = this.setGoal == this.setDone
}