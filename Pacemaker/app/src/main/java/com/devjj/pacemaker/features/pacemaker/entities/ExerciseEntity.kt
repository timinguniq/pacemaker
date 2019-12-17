package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(@PrimaryKey var id: Int,
                          var part:Int,
                          var name:String,
                          var mass:Int,
                          var set:Int,
                          var interval:Int,
                          var achivement: Boolean)