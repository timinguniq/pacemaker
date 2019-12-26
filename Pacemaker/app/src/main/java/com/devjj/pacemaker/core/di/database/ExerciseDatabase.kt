package com.devjj.pacemaker.core.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.usecases.ExerciseDAO

@Database(entities = [ExerciseEntity::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase(){
    abstract fun ExerciseDAO() : ExerciseDAO
}