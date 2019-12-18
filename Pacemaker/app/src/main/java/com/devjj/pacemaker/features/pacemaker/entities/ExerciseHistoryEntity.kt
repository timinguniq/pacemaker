package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exerciseHistories")
class ExerciseHistoryEntity(@PrimaryKey var id: Int,
                            var date:String,
                            var part:Int,
                            var name:String,
                            var mass:Int,
                            var set:Int,
                            var interval:Int,
                            var achivement: Boolean)