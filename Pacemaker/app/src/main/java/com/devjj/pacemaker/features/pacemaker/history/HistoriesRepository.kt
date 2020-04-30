package com.devjj.pacemaker.features.pacemaker.history

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsDatabaseService
import javax.inject.Inject

interface HistoriesRepository {

    fun histories(): Either<Failure, List<History>>
    fun sumOfSetsAndMass() : Either<Failure,SumOfSetsAndMass>
    fun sumOfSetsAndMassOfOneMonth(date:String) : Either<Failure,SumOfSetsAndMass>
    fun totalTimes(date : String) : Either<Failure, TotalTimes>

    class HistoryDatabase
    @Inject constructor(
        private val serviceHistory: HistoryDatabaseService,
        private val serviceStatistics : StatisticsDatabaseService
    ) : HistoriesRepository {
        override fun histories(): Either<Failure, List<History>> {
            return try {
                Right(serviceHistory.histories().map { it.toHistory() })
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

        override fun sumOfSetsAndMass(): Either<Failure, SumOfSetsAndMass> {
            return try{
                Right(serviceHistory.setsAndMass())
            }
            catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

        override fun sumOfSetsAndMassOfOneMonth(date: String): Either<Failure, SumOfSetsAndMass> {
            return try{
                Right(serviceHistory.getOneMonthSumOfSetsAndMass(date))
            }
            catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

        override fun totalTimes(date: String): Either<Failure, TotalTimes> {
            return try{
                Right(serviceStatistics.getTotalTimes(date))
            }catch (exception: Throwable){
                Left(DatabaseError)
            }
        }

    }

}