package com.devjj.pacemaker.features.pacemaker.usecases

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import com.devjj.pacemaker.features.pacemaker.history.SumOfSetsAndMass
import com.devjj.pacemaker.features.pacemaker.historydetail.OneDaySets

@Dao
interface ExerciseHistoryDAO {
    @Query("SELECT * FROM exerciseHistories")
    fun getAll(): List<ExerciseHistoryEntity>

    @Insert(onConflict = REPLACE)
    fun insert(vararg exerciseHistoryEntity: ExerciseHistoryEntity)

    @Delete
    fun delete(exerciseHistoryEntity: ExerciseHistoryEntity)

    @Query("SELECT * FROM exerciseHistories GROUP BY date")
    fun getAllDates(): List<ExerciseHistoryEntity>

    @Query("SELECT * FROM exerciseHistories WHERE date = :date")
    fun getAllByDate(date: String): List<ExerciseHistoryEntity>

    @Query("SELECT Sum(setDone) as sets , Sum(mass*setDone*rep) as mass FROM exerciseHistories ")
    fun getSetsAndMass() : SumOfSetsAndMass

    @Query("SELECT Sum(setDone) as sets FROM exerciseHistories WHERE date = :date")
    fun getOneDaySets(date: String) : OneDaySets

    @Query("SELECT (SELECT Sum(setDone) FROM exerciseHistories  WHERE substr(date,0,8) = substr(:date,0,8) ) as sets ,(SELECT Sum(setDone*mass*rep) FROM exerciseHistories  WHERE substr(date,0,8) = substr(:date,0,8) ) as mass")
    fun getOneMonthSumOfSetsAndMass(date: String) : SumOfSetsAndMass

    // date 검색 후 데이터 삭제하는 함수
    @Query("DELETE FROM exerciseHistories WHERE date = :date")
    fun deleteForDate(date: String)

}