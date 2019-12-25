package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import io.reactivex.Flowable

@Dao
interface ExerciseHistoryDAO{
    @Query("SELECT * FROM exerciseHistories")
    fun getAll(): List<ExerciseHistoryEntity>

    @Insert(onConflict = REPLACE)
    fun insert(vararg exerciseHistoryEntity: ExerciseHistoryEntity)

    @Delete
    fun delete(exerciseHistoryEntity: ExerciseHistoryEntity)
}