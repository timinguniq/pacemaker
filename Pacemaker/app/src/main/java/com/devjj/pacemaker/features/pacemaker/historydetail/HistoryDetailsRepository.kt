package com.devjj.pacemaker.features.pacemaker.historydetail

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.history.HistoryDatabaseService
import kotlinx.coroutines.internal.SynchronizedObject
import javax.inject.Inject
import javax.inject.Singleton


interface HistoryDetailsRepository{

    fun historyDetails(date:String) : Either<Failure, List<HistoryDetail>>
    fun switchAchievement(id : Int) : Either<Failure, Int>
    fun updateAchievementRate(date: String) : Either<Failure,Int>


    class HistoryDetailDatabase
    @Inject constructor(private val service: HistoryDetailDatabaseService):HistoryDetailsRepository{

        @Synchronized
        override fun historyDetails(date:String): Either<Failure, List<HistoryDetail>> {
            return try{
                Right(service.historiesByDate(date).map { it.toHistoryDetail() })
            }catch (exception:Throwable){
                Left(DatabaseError)
            }
        }

        @Synchronized
        override fun switchAchievement(id: Int): Either<Failure, Int> {
            return try{
                Right(service.switchAchievement(id))
            }catch(exception:Throwable){
                Left(DatabaseError)
            }
        }

        @Synchronized
        override fun updateAchievementRate(date : String): Either<Failure, Int> {
            return try{
                Right(service.updateAchievementRate(date))
            }catch(exception : Throwable){
                Left(DatabaseError)
            }
        }
    }
}