package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devjj.pacemaker.features.pacemaker.history.History

@Entity(tableName = "exerciseHistories")
data class ExerciseHistoryEntity(
    @PrimaryKey val id: Int,
    val date: String,
    val part: Int,
    val name: String,
    val mass: Int,
    val set: Int,
    val interval: Int,
    val achivement: Boolean,
    val achivementRate: Int
){
    fun toHistory() = History(id, date, achivementRate)
}