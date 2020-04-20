package com.devjj.pacemaker.features.pacemaker.history

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import javax.inject.Inject

interface HistoriesRepository {

    fun histories(): Either<Failure, List<History>>
    fun summary() : Either<Failure,Summary>
    fun summaryOneMonth(date:String) : Either<Failure,Summary>

    class HistoryDatabase
    @Inject constructor(
        private val service: HistoryDatabaseService
    ) : HistoriesRepository {
        override fun histories(): Either<Failure, List<History>> {
            return try {
                Right(service.histories().map { it.toHistory() })
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

        override fun summary(): Either<Failure, Summary> {
            return try{
                Right(service.summary())
            }
            catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

        override fun summaryOneMonth(date: String): Either<Failure, Summary> {
            return try{
                Right(service.getOneMonthSummary(date))
            }
            catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

    }

}