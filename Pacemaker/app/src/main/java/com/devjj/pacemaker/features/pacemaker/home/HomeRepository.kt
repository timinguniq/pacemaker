package com.devjj.pacemaker.features.pacemaker.home

import android.database.DatabaseErrorHandler
import android.util.Log
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.exception.Failure.SharedPreferencesError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import javax.inject.Inject

interface HomeRepository {
    fun homeData(): Either<Failure, List<HomeData>>
    fun getPlayViewClick(): Either<Failure, Boolean>
    fun setPlayViewClick(isClicked: Boolean): Either<Failure, Unit>


    class DbRepository
    @Inject constructor(private val service: HomeDatabaseService) :
        HomeRepository {
        override fun homeData(): Either<Failure, List<HomeData>> {
            return try{
                Right(service.home().map { it.toHomeData() })
            }catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

        override fun getPlayViewClick(): Either<Failure, Boolean> {
            return try{
                Right(service.getPlayView())
            }catch(exception: Throwable){
                Left(SharedPreferencesError)
            }
        }

        override fun setPlayViewClick(isClicked: Boolean): Either<Failure, Unit> {
            return try{
                Right(service.setPlayView(isClicked))
            }catch(exception: Throwable){
                Left(SharedPreferencesError)
            }
        }
    }

}