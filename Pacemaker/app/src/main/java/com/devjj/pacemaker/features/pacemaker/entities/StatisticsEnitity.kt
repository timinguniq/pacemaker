package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devjj.pacemaker.core.extension.empty

@Entity(tableName = "statistics")
data class StatisticsEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    val date: String,
    val height: Float,
    val weight: Float,
    val totalTime: Int,
    val achievementRate: Int
) {
    companion object {
        fun empty() = StatisticsEntity(0, String.empty(), 0f, 0f, 0, 0)
    }
}
