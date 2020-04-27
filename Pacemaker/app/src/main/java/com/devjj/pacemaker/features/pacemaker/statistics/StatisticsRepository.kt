package com.devjj.pacemaker.features.pacemaker.statistics

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.dialog.Profile
import javax.inject.Inject

interface StatisticsRepository {
    fun statistics() : Either<Failure,List<Statistics>>
    fun updateProfile(date : String, height : Float,weight :Float) : Either<Failure, Unit>

    class DbRepository
    @Inject constructor(private val service: StatisticsDatabaseService) : StatisticsRepository{
        override fun statistics(): Either<Failure, List<Statistics>> {
            return try{
                Right(service.statistics().map { it.toStatistics() })
            }catch(exception : Throwable){
                Left(Failure.DatabaseError)
            }
        }

        override fun updateProfile(date : String, height: Float,weight: Float): Either<Failure, Unit> {
            return try{
                Right(service.updateProfile(date, height,weight))
            }
            catch(exception : Throwable){
                Left(Failure.DatabaseError)
            }
        }
    }
}