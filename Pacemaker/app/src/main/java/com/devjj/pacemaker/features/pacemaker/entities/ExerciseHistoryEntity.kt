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
    val interval: Int
){
    fun toHistory() = History(id, date)
    fun toHistoryDetail() = HistoryDetail(id,date,part,name,mass,rep,setGoal,setDone,interval)
    fun isAcheived() = this.setGoal == this.setDone
}