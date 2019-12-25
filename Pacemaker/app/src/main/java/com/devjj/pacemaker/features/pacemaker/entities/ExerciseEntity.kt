package com.devjj.pacemaker.features.pacemaker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(@PrimaryKey val id: Int,
                          val part:Int,
                          val name:String,
                          val mass:Int,
                          val set:Int,
                          val interval:Int,
                          val achivement: Boolean)