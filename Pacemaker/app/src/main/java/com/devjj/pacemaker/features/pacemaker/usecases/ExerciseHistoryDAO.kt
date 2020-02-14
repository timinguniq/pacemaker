package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import io.reactivex.Flowable

@Dao
interface ExerciseHistoryDAO {
    @Query("SELECT * FROM exerciseHistories")
    fun getAll(): List<ExerciseHistoryEntity>

    @Query("SELECT * FROM exerciseHistories GROUP BY date")
    fun getAllDates(): List<ExerciseHistoryEntity>

    @Query("SELECT * FROM exerciseHistories WHERE date = :date")
    fun getAllByDate(date: String): List<ExerciseHistoryEntity>

    @Query("UPDATE exerciseHistories SET achievement = NOT achievement WHERE id = :id")
    fun switchAchievement(id: Int): Int

    @Query("UPDATE exerciseHistories SET achievementRate = ( (SELECT COUNT(*) FROM exerciseHistories WHERE achievement = 1 AND date = :date)*100/(SELECT COUNT(*) FROM exerciseHistories WHERE date = :date) ) WHERE date = :date")
    fun updateAchievementRate(date: String) : Int

    @Insert(onConflict = REPLACE)
    fun insert(vararg exerciseHistoryEntity: ExerciseHistoryEntity)

    @Delete
    fun delete(exerciseHistoryEntity: ExerciseHistoryEntity)
}