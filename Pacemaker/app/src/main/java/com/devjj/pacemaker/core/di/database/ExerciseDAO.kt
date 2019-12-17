package com.devjj.pacemaker.core.di.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import io.reactivex.Flowable

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM exercises")
    fun getAll(): Flowable<List<ExerciseEntity>>

    @Insert(onConflict = REPLACE)
    fun insert(vararg exerciseEntity: ExerciseEntity)

    @Delete
    fun delete(exerciseEntity: ExerciseEntity)
}