package com.devjj.pacemaker.core.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity

@Database(entities = [ExerciseHistoryEntity::class],version = 1)
abstract class ExerciseHistoryDatabase :RoomDatabase(){
    abstract fun ExerciseHistoryDAO() : ExerciseHistoryDAO
}