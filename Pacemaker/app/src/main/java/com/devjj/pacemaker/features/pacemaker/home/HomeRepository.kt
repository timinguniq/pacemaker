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
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import javax.inject.Inject

interface HomeRepository {
    fun homeData(): Either<Failure, List<HomeData>>

    // DB에 ExerciseData를 삭제하는 함수.
    fun deleteExerciseData(homeData: HomeData): Either<Failure, HomeData>

    // DB에 ExerciseData Swap하는 함수.
    fun swapExerciseData(homeData1Id: Int, homeData2Id: Int) : Either<Failure, HomeData>

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

        override fun deleteExerciseData(homeData: HomeData): Either<Failure, HomeData> {
            service.deleteExerciseData(homeData)
            return try {
                Right(HomeData.empty())
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

        override fun swapExerciseData(
            homeData1Id: Int,
            homeData2Id: Int
        ): Either<Failure, HomeData> {
            service.swapExerciseData(homeData1Id, homeData2Id)
            return try{
                Right(HomeData.empty())
            }catch(exception: Throwable){
                Left(DatabaseError)
            }
        }
    }

}