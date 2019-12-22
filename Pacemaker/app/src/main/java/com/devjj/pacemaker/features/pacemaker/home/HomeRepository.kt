package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import javax.inject.Inject

interface HomeRepository {
    fun homeData() : List<HomeData>

    class DbRepository
    @Inject constructor(private val db: ExerciseDatabase) :
        HomeRepository {
        override fun homeData(): List<HomeData> {
            return db.ExerciseDAO().getAll().map{ it.toHomeData() }
        }
    }
}