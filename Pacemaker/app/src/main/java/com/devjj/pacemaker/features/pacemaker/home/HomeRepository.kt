package com.devjj.pacemaker.features.pacemaker.home

import androidx.annotation.MainThread
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HomeRepository {
    fun exerciseData() : List<ExerciseData>

    class DbRepository
    @Inject constructor(private val db: ExerciseDatabase) :
        HomeRepository {
        override fun exerciseData(): List<ExerciseData> {
            return db.ExerciseDAO().getAll().map{ it.toExerciseData() }
        }
    }
}