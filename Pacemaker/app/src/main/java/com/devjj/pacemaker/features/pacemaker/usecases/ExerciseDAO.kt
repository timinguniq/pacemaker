package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.history.SumOfSetsAndMass
import io.reactivex.Flowable

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM exercises")
    fun getAll(): List<ExerciseEntity>

    @Insert(onConflict = REPLACE)
    fun insert(vararg exerciseEntity: ExerciseEntity)

    @Update
    fun update(vararg exerciseEntity: ExerciseEntity)

    @Delete
    fun delete(exerciseEntity: ExerciseEntity)

    @Query("DELETE FROM exercises")
    fun deleteAll()

    @Query("SELECT * FROM exercises WHERE id=:id")
    fun searchData(id: Int): ExerciseEntity

    // id값 변경하는 query
    @Query("UPDATE exercises SET id=:updateId WHERE id=:id")
    fun updateId(id: Int, updateId: Int)

}