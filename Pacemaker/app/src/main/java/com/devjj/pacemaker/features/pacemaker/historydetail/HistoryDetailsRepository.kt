package com.devjj.pacemaker.features.pacemaker.historydetail

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import com.devjj.pacemaker.features.pacemaker.statistics.Statistics
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsDatabaseService
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject


interface HistoryDetailsRepository{

    fun historyDetails(date:String) : Either<Failure, List<HistoryDetail>>

    fun getOneDaySets(date:String) : Either<Failure,OneDaySets>
    fun getStatisticsOneDay(date : String) : Either<Failure,StatisticsOneDay>

    class HistoryDetailDatabase
    @Inject constructor(private val serviceHistoryDetail: HistoryDetailDatabaseService, private val serviceStatistics :StatisticsDatabaseService ):HistoryDetailsRepository{

        override fun historyDetails(date:String): Either<Failure, List<HistoryDetail>> {

            return try{
                Right(serviceHistoryDetail.historiesByDate(date).map { it.toHistoryDetail() })
            }catch (exception:Throwable){
                Left(DatabaseError)
            }
        }

        override fun getOneDaySets(date: String): Either<Failure, OneDaySets> {
            return try{
                Right(serviceHistoryDetail.getOneDaySets(date))
            }
            catch(exception : Throwable) {
                Left(DatabaseError)
            }
        }

        override fun getStatisticsOneDay(date: String): Either<Failure, StatisticsOneDay> {
            return try{
                Right(serviceStatistics.getStatisticsOneDay(date).toStatisticsOneDay())
            }catch (exception : Throwable){
                Left(DatabaseError)
            }
        }

    }
}