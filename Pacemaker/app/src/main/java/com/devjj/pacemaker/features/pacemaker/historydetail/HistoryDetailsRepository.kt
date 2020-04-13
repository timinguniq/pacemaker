package com.devjj.pacemaker.features.pacemaker.historydetail

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject


interface HistoryDetailsRepository{

    fun historyDetails(date:String) : Either<Failure, List<HistoryDetail>>
    fun switchAchievement(id : Int) : Either<Failure, Int>
    fun updateAchievementRate(date: String) : Either<Failure,Int>
    fun getOneDaySummary(date:String) : Either<Failure,OneDaySummary>


    class HistoryDetailDatabase
    @Inject constructor(private val service: HistoryDetailDatabaseService):HistoryDetailsRepository{

        override fun historyDetails(date:String): Either<Failure, List<HistoryDetail>> {

            return try{
                Right(service.historiesByDate(date).map { it.toHistoryDetail() })
            }catch (exception:Throwable){
                Left(DatabaseError)
            }
        }

        override fun switchAchievement(id: Int): Either<Failure, Int> {
            return try{
                Right(service.switchAchievement(id))
            }catch(exception:Throwable){
                Left(DatabaseError)
            }
        }

        override fun updateAchievementRate(date : String): Either<Failure, Int> {
            return try{
                Right(service.updateAchievementRate(date))
            }catch(exception : Throwable){
                Left(DatabaseError)
            }
        }

        override fun getOneDaySummary(date: String): Either<Failure, OneDaySummary> {
            return try{
                Right(service.getOneDaySummary(date))
            }
            catch(exception : Throwable) {
                Left(DatabaseError)
            }
        }
    }
}