package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import io.reactivex.Flowable

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM exercises")
    fun getAll(): List<ExerciseEntity>

    @Insert(onConflict = REPLACE)
    fun insert(vararg exerciseEntity: ExerciseEntity)

    @Update
    fun update(vararg exerciseEntitiy: ExerciseEntity)

    @Delete
    fun delete(exerciseEntity: ExerciseEntity)

    @Query("DELETE FROM exercises")
    fun deleteAll()

    @Query("SELECT * FROM exercises WHERE id=:id")
    fun searchData(id: Int): ExerciseEntity
}